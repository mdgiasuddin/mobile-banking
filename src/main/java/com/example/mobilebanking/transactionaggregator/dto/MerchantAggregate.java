package com.example.mobilebanking.transactionaggregator.dto;

import com.example.mobilebanking.common.dto.TransactionEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(doNotUseGetters = true)
public class MerchantAggregate {
    public BigDecimal totalAmount;
    public long count;

    public MerchantAggregate add(TransactionEvent tranasaction) {
        totalAmount = totalAmount.add(tranasaction.getAmount());
        count++;
        return this;
    }
}


