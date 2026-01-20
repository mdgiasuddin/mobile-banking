package com.example.mobilebanking.transactionprocessor.service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.example.mobilebanking.common.dto.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.datastax.oss.driver.api.core.ConsistencyLevel.QUORUM;
import static com.datastax.oss.driver.api.core.ConsistencyLevel.SERIAL;
import static com.example.mobilebanking.transactionprocessor.enumeration.TransactionDirection.CREDIT;
import static com.example.mobilebanking.transactionprocessor.enumeration.TransactionDirection.DEBIT;
import static com.example.mobilebanking.transactionprocessor.enumeration.TransactionStatus.FAILED;
import static com.example.mobilebanking.transactionprocessor.enumeration.TransactionStatus.PROCESSING;
import static com.example.mobilebanking.transactionprocessor.enumeration.TransactionStatus.SUCCEEDED;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProcessorService {

    private final BalanceService balanceService;
    private final CqlSession session;

    public void processTransaction(TransactionEvent transaction) {
        if (!registerTransaction(transaction)) {
            log.warn("Transaction {} already processed", transaction.getTransactionId());
            return;
        }

        try {
            balanceService.applyBalances(transaction);
            updateTransactionStatus(transaction.getTransactionId(), PROCESSING.name(), SUCCEEDED.name(), null);
        } catch (Exception e) {
            log.error("Error processing transaction {}", transaction.getTransactionId(), e);
            updateTransactionStatus(transaction.getTransactionId(), PROCESSING.name(), FAILED.name(), e.getMessage());
            return;
        }

        insertLedgerEntry(transaction, true);
        insertLedgerEntry(transaction, false);
    }


    public boolean registerTransaction(TransactionEvent transaction) {
        SimpleStatement statement = SimpleStatement.builder("""
                        insert into transaction_status (
                            id, from_account_id, from_account_number,
                            to_account_id, to_account_number, amount,
                            transaction_time, type, status, created_at
                        )
                        values (?, ?, ?, ?, ?, ?, ?, ?, ?, toTimestamp(now()))
                        if not exists
                        """)
                .addPositionalValues(
                        transaction.getTransactionId(),
                        transaction.getFromAccountId(),
                        transaction.getFromAccountNumber(),
                        transaction.getToAccountId(),
                        transaction.getToAccountNumber(),
                        transaction.getAmount(),
                        transaction.getEventTime(),
                        transaction.getTransactionType(),
                        PROCESSING.name()
                )
                .build();

        ResultSet rs = session.execute(statement);
        return rs.wasApplied();
    }

    public void updateTransactionStatus(
            UUID transactionId,
            String currentStatus,
            String newStatus,
            String failureReason
    ) {
        String cql = """
                update transaction_status
                set status = ?, failure_reason = ?
                where id = ?
                if status = ?
                """;

        PreparedStatement ps = session.prepare(cql);

        BoundStatement stmt = ps.bind(
                        newStatus,
                        failureReason,
                        transactionId,
                        currentStatus
                ).setConsistencyLevel(QUORUM)
                .setSerialConsistencyLevel(SERIAL);

        ResultSet rs = session.execute(stmt);
        if (rs.wasApplied()) {
            log.info("Transaction {} status updated: {} → {}", transactionId, currentStatus, newStatus);
        } else {
            log.warn("Status update failed for transaction {} (expected={})", transactionId, currentStatus);
        }
    }

    public void insertLedgerEntry(TransactionEvent transaction, boolean isForward) {

        String transactionDate = transaction.getEventTime()
                .atZone(ZoneId.of("Asia/Dhaka"))
                .format(DateTimeFormatter.ofPattern("yyyyMM"));

        SimpleStatement stmt = SimpleStatement.builder("""
                        insert into transaction_ledger (
                            account_id, account_number, transaction_date,
                            transaction_time, transaction_id,counterparty_id,
                            counterparty_account_number,amount, direction,
                            transaction_type, created_at
                        )
                        values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, toTimestamp(now()))
                        """)
                .addPositionalValues(
                        isForward ? transaction.getFromAccountId() : transaction.getToAccountId(),
                        isForward ? transaction.getFromAccountNumber() : transaction.getToAccountNumber(),
                        transactionDate,
                        transaction.getEventTime(),
                        transaction.getTransactionId(),
                        isForward ? transaction.getToAccountId() : transaction.getFromAccountId(),
                        isForward ? transaction.getToAccountNumber() : transaction.getFromAccountNumber(),
                        transaction.getAmount(),
                        isForward ? DEBIT.name() : CREDIT.name(),
                        transaction.getTransactionType()
                )
                .build();

        session.execute(stmt);
    }


}
