package com.example.mobilebanking.transactionprocessor.listener;

import com.example.mobilebanking.common.dto.TransactionEvent;
import com.example.mobilebanking.transactionprocessor.service.TransactionProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionListener {

    private final TransactionProcessorService transactionProcessorService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "transaction", groupId = "transaction-group")
    public void listen(ConsumerRecord<String, byte[]> record) {
        TransactionEvent transaction = objectMapper.readValue(record.value(), TransactionEvent.class);
        log.info("Processing transaction: {}", transaction);
        transactionProcessorService.processTransaction(transaction);
    }
}
