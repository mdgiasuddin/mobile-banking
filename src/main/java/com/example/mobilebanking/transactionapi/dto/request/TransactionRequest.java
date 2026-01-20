package com.example.mobilebanking.transactionapi.dto.request;

import com.example.mobilebanking.common.enumeration.TransactionType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull
        Long fromAccountId,
        @NotNull
        Long toAccountId, // merchantId
        @NotNull
        TransactionType type,        // P2P or P2M
        @NotNull
        @Digits(integer = 6, fraction = 2)
        BigDecimal amount
) {
}
