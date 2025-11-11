package com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FXDealsResponseDto {
    private UUID id;
    private String orderingCurrencyIsoCode;
    private String toCurrencyIsoCode;
    private Double dealAmount;
    private LocalDateTime dealTimestamp;
    private LocalDateTime createdAt;
}

