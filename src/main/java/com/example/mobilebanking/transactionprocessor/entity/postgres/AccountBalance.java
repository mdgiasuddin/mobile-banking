package com.example.mobilebanking.transactionprocessor.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table
@Getter
@Setter
public class AccountBalance {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private long accountId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Version
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private long version;
}
