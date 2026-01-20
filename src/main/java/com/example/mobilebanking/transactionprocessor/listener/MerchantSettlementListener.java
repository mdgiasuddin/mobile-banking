package com.example.mobilebanking.transactionprocessor.listener;

import com.example.mobilebanking.common.dto.MerchantSettlementEvent;
import com.example.mobilebanking.transactionprocessor.service.MerchantSettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantSettlementListener {
    private final MerchantSettlementService merchantSettlementService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "merchant-settlement", groupId = "merchant-settlement-group")
    public void listen(ConsumerRecord<String, byte[]> record) {
        MerchantSettlementEvent event = objectMapper.readValue(record.value(), MerchantSettlementEvent.class);

        log.info("Received message: {}", event);
        merchantSettlementService.processMerchantSettlement(event);
    }
}
