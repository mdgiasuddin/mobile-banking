package com.example.mobilebanking.transactionapi.dto.response;

import java.util.UUID;

public record TransactionResponse(
        UUID transactionId
) {
}
