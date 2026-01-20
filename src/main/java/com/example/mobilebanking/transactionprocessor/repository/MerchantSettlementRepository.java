package com.example.mobilebanking.transactionprocessor.repository;

import com.example.mobilebanking.transactionprocessor.entity.postgres.MerchantSettlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MerchantSettlementRepository extends JpaRepository<MerchantSettlement, Long> {
    boolean existsBySettlementKey(UUID settlementKey);
}
