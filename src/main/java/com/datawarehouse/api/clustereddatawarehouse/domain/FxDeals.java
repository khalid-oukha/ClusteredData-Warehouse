package com.datawarehouse.api.clustereddatawarehouse.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fx_deals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FxDeals {
    @Id
    private UUID id;

    @Column(nullable = false, length = 3)
    private String orderingCurrencyIsoCode;

    @Column(nullable = false, length = 3)
    private String toCurrencyIsoCode;

    @Column(nullable = false)
    private Double dealAmount;

    @Column(nullable = false)
    private LocalDateTime dealTimestamp;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
