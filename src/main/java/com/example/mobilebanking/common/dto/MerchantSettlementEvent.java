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
public class MerchantSettlementEvent {
    private UUID settlementKey;
    private String merchantId;
    private BigDecimal totalAmount;
    private long transactionCount;
    private Instant emittedAt;
}


