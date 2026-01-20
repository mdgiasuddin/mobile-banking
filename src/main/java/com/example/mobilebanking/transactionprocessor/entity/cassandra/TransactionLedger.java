package com.example.mobilebanking.transactionprocessor.entity.cassandra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("transaction_ledger")
public class TransactionLedger {

    @PrimaryKey
    private TransactionLedgerKey key;

    @Column("account_number")
    private String accountNumber;

    @Column("counterparty_id")
    private Long counterpartyId;

    @Column("counterparty_account_number")
    private String counterpartyAccountNumber;

    private BigDecimal amount;
    private String direction;   // DEBIT / CREDIT
    private String txnType;     // P2P / P2M
}

