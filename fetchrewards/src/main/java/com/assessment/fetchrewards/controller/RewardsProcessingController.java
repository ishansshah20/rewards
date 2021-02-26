package com.assessment.fetchrewards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.assessment.fetchrewards.dto.PayerBalancesResponseDTO;
import com.assessment.fetchrewards.dto.SpentPointsResponseDTO;
import com.assessment.fetchrewards.dto.PointsSpendDTO;
import com.assessment.fetchrewards.model.Transaction;
import com.assessment.fetchrewards.repository.TransactionRepository;
import com.assessment.fetchrewards.service.RewardsProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class RewardsProcessingController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RewardsProcessingService rewardsProcessingService;


    @PostMapping("/add/transaction")
    public ResponseEntity addTransaction(@RequestBody Transaction transaction){
        SpentPointsResponseDTO spentPointsResponseDTO = rewardsProcessingService.addTransactionService(transaction);
        if (!spentPointsResponseDTO.isSuccess()){
            return new ResponseEntity(spentPointsResponseDTO, HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity(spentPointsResponseDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/spend/rewards")
    public ResponseEntity spendRewards(@RequestBody PointsSpendDTO pointsSpendDTO){
        SpentPointsResponseDTO spentPointsResponseDTO = rewardsProcessingService.spendRewardsService(pointsSpendDTO);
        if(!spentPointsResponseDTO.isSuccess()){
            return new ResponseEntity(spentPointsResponseDTO, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(spentPointsResponseDTO, HttpStatus.OK);
        }

    }

    @GetMapping("/get/balance")
    public ResponseEntity getBalance() throws JsonProcessingException {
        PayerBalancesResponseDTO payerBalance =  rewardsProcessingService.getPayerBalance();
        if (!payerBalance.isSuccess()){
            return new ResponseEntity(payerBalance, HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity(payerBalance, HttpStatus.OK);
        }
    }
}
