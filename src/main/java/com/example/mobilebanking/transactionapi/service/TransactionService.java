package com.example.mobilebanking.transactionapi.service;

import com.example.mobilebanking.common.dto.TransactionEvent;
import com.example.mobilebanking.transactionapi.dto.request.TransactionRequest;
import com.example.mobilebanking.transactionapi.dto.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public TransactionResponse sendTransaction(TransactionRequest request) {
        TransactionEvent transaction = new TransactionEvent();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setFromAccountId(request.fromAccountId());
        transaction.setFromAccountNumber("Account-" + request.fromAccountId());
        transaction.setToAccountId(request.toAccountId());
        transaction.setToAccountNumber("Account-" + request.toAccountId());
        transaction.setAmount(request.amount());
        transaction.setTransactionType(request.type().name());
        transaction.setEventTime(Instant.now());

        log.info("Sending transaction {}", transaction);
        kafkaTemplate.send("transaction", transaction.getTransactionId().toString(), transaction);

        return new TransactionResponse(transaction.getTransactionId());
    }

    /*public TransactionResponse sendTransactionTest(TransactionRequest request) {
        TransactionEvent transaction = new TransactionEvent();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setFromAccount(request.fromAccountId());
        transaction.setToAccount(request.toAccountId());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setEventTime(Instant.now());

        log.info("Sending transaction {}", transaction);
        kafkaTemplate.send("transaction-test", transaction.getTransactionId(), transaction);
    }*/
}
