package com.fetchrewards.rewards.service;

import com.fetchrewards.rewards.dto.PayerBalancesResponseDTO;
import com.fetchrewards.rewards.dto.SpentPointsResponseDTO;
import com.fetchrewards.rewards.dto.PointsSpendDTO;
import com.fetchrewards.rewards.dto.SpentPointsDTO;
import com.fetchrewards.rewards.model.PayerBalance;
import com.fetchrewards.rewards.model.Transaction;
import com.fetchrewards.rewards.repository.PayerBalanceRepository;
import com.fetchrewards.rewards.repository.TransactionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class RewardsProcessingService {
    Logger log = LogManager.getLogger(RewardsProcessingService.class);

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PayerBalanceRepository payerBalanceRepository;

    public SpentPointsResponseDTO addTransactionService(Transaction transaction){
       try {
           transactionRepository.save(transaction);

           //Update payer Balance
           Optional<PayerBalance> optionalPayerBalance = payerBalanceRepository.findByName(transaction.getPayer());
           if (optionalPayerBalance.isPresent()) {
               PayerBalance payerBalance = optionalPayerBalance.get();
               long totalBalance = payerBalance.getBalance() + transaction.getPoints();

               if (totalBalance > 0){
                   payerBalance.setBalance(payerBalance.getBalance() + transaction.getPoints());
               }
               else{
                   payerBalance.setBalance(0);
               }
               payerBalanceRepository.save(payerBalance);

           } else {
               PayerBalance payerBalance = new PayerBalance();
               payerBalance.setName(transaction.getPayer());

               long points = transaction.getPoints();
               if (points > 0){
                   payerBalance.setBalance(points);
               }
               else{
                   payerBalance.setBalance(0);
               }
               payerBalanceRepository.save(payerBalance);
           }

           SpentPointsResponseDTO responseMessage = new SpentPointsResponseDTO();
           responseMessage.setSuccess(true);
           responseMessage.setMessage("Transaction Saved!");
           responseMessage.setError("");
           responseMessage.setStatus(HttpStatus.OK);
           responseMessage.setResponse(new ArrayList());
           return responseMessage;

       }catch(Exception e){
           log.error("Error saving the transaction",e);
           SpentPointsResponseDTO responseMessage = new SpentPointsResponseDTO();
           responseMessage.setSuccess(false);
           responseMessage.setMessage("Error saving the transaction!");
           responseMessage.setError(e.getMessage());
           responseMessage.setStatus(HttpStatus.BAD_REQUEST);
           responseMessage.setResponse(new ArrayList());
           return responseMessage;
       }
    }

    public SpentPointsResponseDTO spendRewardsService(PointsSpendDTO pointsSpendDTO) {
        HashMap<String, Long> balanceMap = new HashMap<String, Long>();
        HashMap<String, Long> resultMap = new HashMap<String, Long>();
        List<Object> response = new ArrayList();

        // Getting those transactions only that is still in need of processing
        try {
            Optional<List<Transaction>> optionalTransactions = transactionRepository.findAllNotProcessed();


            Iterable<PayerBalance> iterablePayerBalance = payerBalanceRepository.findAll();
            Iterator<PayerBalance> payerBalanceIterator = iterablePayerBalance.iterator();
            List<Transaction> transactions = new ArrayList<Transaction>();


            if (optionalTransactions.isPresent()) {
                transactions = optionalTransactions.get();
            } else {
                SpentPointsResponseDTO error = new SpentPointsResponseDTO();
                error.setMessage("Not enough balance!");
                response.add(error);

            }

            int spendingPoints = pointsSpendDTO.getPoints();
            long totalPoints = 0;

            // Add payer data into a hashmap and calculating total counts. It removes the bottle next to constantly read from database
            while (payerBalanceIterator.hasNext()) {
                PayerBalance payerBalance = payerBalanceIterator.next();
                balanceMap.put(payerBalance.getName(), payerBalance.getBalance());
                totalPoints += payerBalance.getBalance();
            }

            // Main Logic
            if (totalPoints >= spendingPoints) {
                long remainingPoints = spendingPoints;
                for (Transaction t : transactions) {
                    long points = Math.min(t.getPoints(), balanceMap.get(t.getPayer()));
                    if (points <= remainingPoints) {
                        remainingPoints -= points;
                        balanceMap.put(t.getPayer(), balanceMap.get(t.getPayer()) - points);
                        resultMap.put(t.getPayer(), resultMap.getOrDefault(t.getPayer(), 0l) - points);
                        t.setProcessed(true);
                        transactionRepository.save(t);
                    } else {
                        balanceMap.put(t.getPayer(), balanceMap.get(t.getPayer()) - remainingPoints);
                        resultMap.put(t.getPayer(), resultMap.getOrDefault(t.getPayer(), 0l) - remainingPoints);
                        break;
                    }
                }

                // Save updated data from the hashmap back to database
                Iterator<PayerBalance> payerBalanceIterator2 = iterablePayerBalance.iterator();
                while (payerBalanceIterator2.hasNext()) {
                    PayerBalance payerBalance = payerBalanceIterator2.next();
                    payerBalance.setBalance(balanceMap.get(payerBalance.getName()));
                    payerBalanceRepository.save(payerBalance);
                }

                // Convert resultMap to Java class DTO for response
                for (String key: resultMap.keySet()){
                    SpentPointsDTO spentPoints = new SpentPointsDTO();
                    spentPoints.setPayer(key);
                    spentPoints.setPoints(resultMap.get(key));
                    response.add(spentPoints);
                }

                SpentPointsResponseDTO responseMessage = new SpentPointsResponseDTO();
                responseMessage.setSuccess(true);
                responseMessage.setMessage("Rewards spent!");
                responseMessage.setError("");
                responseMessage.setStatus(HttpStatus.OK);
                responseMessage.setResponse(response);
                return responseMessage;

            } else {
                SpentPointsResponseDTO responseMessage = new SpentPointsResponseDTO();
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Not enough balance");
                responseMessage.setError("Logic error with the rewards spent");
                responseMessage.setStatus(HttpStatus.BAD_REQUEST);
                responseMessage.setResponse(new ArrayList());
                return responseMessage;
            }

        }catch(Exception e){
            log.error("Error Spending Points", e);
            SpentPointsResponseDTO responseMessage = new SpentPointsResponseDTO();
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Error spending order");
            responseMessage.setError(e.getMessage());
            responseMessage.setStatus(HttpStatus.BAD_REQUEST);
            responseMessage.setResponse(new ArrayList());
            return responseMessage;
        }
    }

    public PayerBalancesResponseDTO getPayerBalance() throws JsonProcessingException {
        try {
            HashMap<String, Long> balanceMap = new HashMap<String, Long>();
            Iterator<PayerBalance> payerBalanceIterator = payerBalanceRepository.findAll().iterator();
            List<Object> response = new ArrayList();

            while (payerBalanceIterator.hasNext()) {
                // To cut the cost of pinging the database
                PayerBalance payerBalance = payerBalanceIterator.next();
                balanceMap.put(payerBalance.getName(), payerBalance.getBalance());
            }
            PayerBalancesResponseDTO responseMessage = new PayerBalancesResponseDTO();
            responseMessage.setSuccess(true);
            responseMessage.setMessage("Rewards spent!");
            responseMessage.setError("");
            responseMessage.setStatus(HttpStatus.OK);
            responseMessage.setResponse(balanceMap);
            return responseMessage;


        }catch(Exception e){
            log.error("Error getting balance",e);
            PayerBalancesResponseDTO responseMessage = new PayerBalancesResponseDTO();
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Error getting balance!");
            responseMessage.setError(e.getMessage());
            responseMessage.setStatus(HttpStatus.BAD_REQUEST);
            responseMessage.setResponse(new HashMap());
            return responseMessage;
        }
    }
}
