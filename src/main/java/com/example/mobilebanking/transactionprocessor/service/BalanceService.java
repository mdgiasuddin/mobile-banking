package com.example.mobilebanking.transactionprocessor.service;

import com.example.mobilebanking.common.dto.TransactionEvent;
import com.example.mobilebanking.transactionprocessor.entity.postgres.AccountBalance;
import com.example.mobilebanking.transactionprocessor.exception.TransactionException;
import com.example.mobilebanking.transactionprocessor.repository.AccountBalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {
    private final AccountBalanceRepository accountBalanceRepository;

    @Transactional
    public void applyBalances(TransactionEvent transaction) {

        AccountBalance fromAccount = accountBalanceRepository
                .findByAccountId(transaction.getFromAccountId())
                .orElseThrow(() -> new TransactionException("Account " + transaction.getFromAccountId() + " not found"));
        ;

        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        accountBalanceRepository.save(fromAccount);

        if (!transaction.isP2M()) {
            AccountBalance toAccount = accountBalanceRepository
                    .findByAccountId(transaction.getToAccountId())
                    .orElseThrow(() -> new TransactionException("Account " + transaction.getToAccountId() + " not found"));
            ;

            toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
            accountBalanceRepository.save(toAccount);
        }
    }
}
