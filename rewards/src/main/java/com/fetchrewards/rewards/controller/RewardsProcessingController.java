package com.fetchrewards.rewards.controller;

import com.fetchrewards.rewards.dto.PointsSpendDTO;
import com.fetchrewards.rewards.model.Transaction;
import com.fetchrewards.rewards.repository.TransactionRepository;
import com.fetchrewards.rewards.service.RewardsProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
public class RewardsProcessingController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RewardsProcessingService rewardsProcessingService;


    @PostMapping("/add/transaction")
    public String addTransaction(@RequestBody Transaction transaction){
        return rewardsProcessingService.addTransactionService(transaction);
    }

    @PostMapping("/spend/rewards")
    public HashMap spendRewards(@RequestBody PointsSpendDTO pointsSpendDTO){
        return rewardsProcessingService.spendRewardsService(pointsSpendDTO);
    }

    @GetMapping("/get/balance")
    public HashMap getBalance(){
        return rewardsProcessingService.getPayerBalance();
    }
}
