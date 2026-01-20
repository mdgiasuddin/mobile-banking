package com.example.mobilebanking.transactionapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("transaction_status")
public class TransactionStatus {

    @PrimaryKey
    private UUID transactionId;

    @Column("account_id")
    private Long accountId;

    @Column("account_number")
    private String accountNumber;

    @Column("counterparty_id")
    private Long counterpartyId;

    @Column("counterparty_account_number")
    private String counterpartyAccountNumber;

    private BigDecimal amount;

    private String direction;   // DEBIT / CREDIT
    private String txnType;     // P2P / P2M
    private String status;      // INITIATED / PROCESSING / SUCCESS / FAILED

    @Column("failure_reason")
    private String failureReason;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
}

