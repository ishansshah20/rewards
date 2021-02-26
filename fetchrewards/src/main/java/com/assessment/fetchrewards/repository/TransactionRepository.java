package com.assessment.fetchrewards.repository;

import com.assessment.fetchrewards.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends CrudRepository<Transaction, UUID> {

    @Query("select t from Transaction t where t.isProcessed=false order by t.timestamp")
    Optional<List<Transaction>> findAllNotProcessed();


}
