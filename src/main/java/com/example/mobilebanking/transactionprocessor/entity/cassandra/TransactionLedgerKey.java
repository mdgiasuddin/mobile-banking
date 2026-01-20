package com.example.mobilebanking.transactionprocessor.entity.cassandra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.Ordering.DESCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyClass
public class TransactionLedgerKey implements Serializable {

    @PrimaryKeyColumn(name = "account_id", ordinal = 0, type = PARTITIONED)
    private Long accountId;

    @PrimaryKeyColumn(name = "txn_date", ordinal = 1, type = PARTITIONED)
    private String txnDate;  // YYYYMM bucket

    @PrimaryKeyColumn(name = "transaction_time", ordinal = 2, type = CLUSTERED, ordering = DESCENDING)
    private Instant transactionTime;

    @PrimaryKeyColumn(name = "transaction_id", ordinal = 3, type = CLUSTERED)
    private UUID transactionId;
}

