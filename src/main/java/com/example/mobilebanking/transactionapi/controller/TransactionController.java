package com.example.mobilebanking.transactionapi.controller;

import com.example.mobilebanking.transactionapi.dto.request.TransactionRequest;
import com.example.mobilebanking.transactionapi.dto.response.TransactionResponse;
import com.example.mobilebanking.transactionapi.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @RequestMapping("/send")
    public TransactionResponse sendTransaction(@Valid @RequestBody TransactionRequest request) {
        return transactionService.sendTransaction(request);
    }

    /*@RequestMapping("/test")
    public void sendTransactionTest(@Valid @RequestBody TransactionRequest request) {
        transactionService.sendTransactionTest(request);
    }*/
}
