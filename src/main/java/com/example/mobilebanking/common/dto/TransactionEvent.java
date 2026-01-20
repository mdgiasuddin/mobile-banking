package com.example.mobilebanking.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(doNotUseGetters = true)
public class TransactionEvent {

    private UUID transactionId;
    private long fromAccountId;
    private String fromAccountNumber;
    private long toAccountId;
    private String toAccountNumber;
    private BigDecimal amount;
    private String transactionType;        // P2P or P2M
    private String failureReason;
    private Instant eventTime;

    public boolean isP2M() {
        return "P2M".equalsIgnoreCase(transactionType);
    }
}

