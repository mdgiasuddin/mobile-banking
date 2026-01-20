package com.example.mobilebanking.transactionprocessor.service;

import com.example.mobilebanking.common.dto.MerchantSettlementEvent;
import com.example.mobilebanking.transactionprocessor.entity.postgres.MerchantSettlement;
import com.example.mobilebanking.transactionprocessor.repository.MerchantSettlementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantSettlementService {

    private final MerchantSettlementRepository merchantSettlementRepository;

    public void processMerchantSettlement(MerchantSettlementEvent event) {
        ZonedDateTime windowEnd = ZonedDateTime.ofInstant(event.getEmittedAt(), ZoneId.of("Asia/Dhaka"));

        if (merchantSettlementRepository.existsBySettlementKey(event.getSettlementKey())) {
            throw new IllegalArgumentException("Duplicate settlement for merchant: " + event.getMerchantId());
        }

        MerchantSettlement merchantSettlement = new MerchantSettlement();
        merchantSettlement.setSettlementKey(event.getSettlementKey());
        merchantSettlement.setMerchantId(event.getMerchantId());
        merchantSettlement.setWindowEnd(windowEnd);
        merchantSettlement.setTotalAmount(event.getTotalAmount());
        merchantSettlement.setTransactionCount(event.getTransactionCount());

        merchantSettlementRepository.save(merchantSettlement);
        log.info("Saved settlement {}", merchantSettlement);
    }
}
