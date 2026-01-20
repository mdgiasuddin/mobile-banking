package com.example.mobilebanking.transactionprocessor.entity.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "merchant_settlement")
@Getter
@Setter
@ToString(doNotUseGetters = true)
public class MerchantSettlement {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private UUID settlementKey;
    private String merchantId;
    private ZonedDateTime windowEnd;
    private BigDecimal totalAmount;
    private long transactionCount;
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
