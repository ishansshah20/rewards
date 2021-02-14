package com.fetchrewards.rewards.repository;

import com.fetchrewards.rewards.model.PayerBalance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PayerBalanceRepository extends CrudRepository<PayerBalance, UUID> {

    @Query("select c from PayerBalance c where c.name=:name")
    Optional<PayerBalance> findByName(@Param("name") String payer);
}
