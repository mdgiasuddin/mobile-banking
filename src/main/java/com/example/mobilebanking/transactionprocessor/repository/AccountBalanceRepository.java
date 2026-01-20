package com.example.mobilebanking.transactionprocessor.repository;

import com.example.mobilebanking.transactionprocessor.entity.postgres.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    @Lock(PESSIMISTIC_WRITE)
    Optional<AccountBalance> findByAccountId(Long accountId);
}
