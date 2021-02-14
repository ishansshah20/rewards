package com.fetchrewards.rewards.service;

import com.fetchrewards.rewards.dto.PointsSpendDTO;
import com.fetchrewards.rewards.model.PayerBalance;
import com.fetchrewards.rewards.model.Transaction;
import com.fetchrewards.rewards.repository.PayerBalanceRepository;
import com.fetchrewards.rewards.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RewardsProcessingService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PayerBalanceRepository payerBalanceRepository;

    public String addTransactionService(Transaction transaction){
       Transaction savedTransaction;

       try {
           savedTransaction = transactionRepository.save(transaction);
           //Update payer Balance
           Optional<PayerBalance> optionalPayerBalance = payerBalanceRepository.findByName(transaction.getPayer());
           if (optionalPayerBalance.isPresent()) {
               PayerBalance payerBalance = optionalPayerBalance.get();
               payerBalance.setBalance(payerBalance.getBalance() + transaction.getPoints());
               payerBalanceRepository.save(payerBalance);
           } else {
               PayerBalance payerBalance = new PayerBalance();
               payerBalance.setName(transaction.getPayer());
               payerBalance.setBalance(transaction.getPoints());
               payerBalanceRepository.save(payerBalance);
           }

       }catch(Exception e){
           throw new RuntimeException(e);
       }

       if (savedTransaction == null) {
           return "Error saving the transaction";
       }
       else{
           return "Transaction Saved";
       }

    }

    public HashMap spendRewardsService(PointsSpendDTO pointsSpendDTO) {
        HashMap<String, Long> balanceMap = new HashMap<String, Long>();
        HashMap<String, Long> resultMap = new HashMap<String, Long>();

        // Getting those transactions only that is still in need of processing
        try {
            Optional<List<Transaction>> optionalTransactions = transactionRepository.findAllNotProcessed();

            Iterable<PayerBalance> iterablePayerBalance = payerBalanceRepository.findAll();
            Iterator<PayerBalance> payerBalanceIterator = iterablePayerBalance.iterator();
            List<Transaction> transactions = new ArrayList<Transaction>();


            if (optionalTransactions.isPresent()) {
                transactions = optionalTransactions.get();
            } else {
                HashMap<String,String> returnMap = new HashMap<>();
                returnMap.put("Error","Not enough balance!");
                return returnMap;

            }

            int spendingPoints = pointsSpendDTO.getPoints();
            long totalPoints = 0;

            // Add payer data into a hashmap and caolculating total counts. It removes the bottle next to constantly read from database
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

            } else {
                HashMap<String,String> returnMap = new HashMap<>();
                returnMap.put("Error","Not enough balance!");
                return returnMap;
            }


        }catch(Exception e){
            throw new RuntimeException(e);
        }

        return resultMap;
    }

    public HashMap getPayerBalance(){
        HashMap<String, Long> balanceMap = new HashMap<String, Long>();
        try {
            Iterator<PayerBalance> payerBalanceIterator = payerBalanceRepository.findAll().iterator();

            while (payerBalanceIterator.hasNext()) {
                // To cut the cost of pinging the database
                PayerBalance payerBalance = payerBalanceIterator.next();
                balanceMap.put(payerBalance.getName(), payerBalance.getBalance());
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return balanceMap;
    }
}
