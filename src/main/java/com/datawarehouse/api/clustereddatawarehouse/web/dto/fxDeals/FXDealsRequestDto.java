package com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FXDealsRequestDto {
    @NotNull(message = "Deal Unique Id is required")
    private UUID dealUniqueId;

    @NotBlank(message = "Ordering Currency ISO Code is required")
    @Size(min = 3, max = 3, message = "Currency ISO Code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency ISO Code must be 3 uppercase letters")
    private String orderingCurrencyIsoCode;

    @NotBlank(message = "To Currency ISO Code is required")
    @Size(min = 3, max = 3, message = "Currency ISO Code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency ISO Code must be 3 uppercase letters")
    private String toCurrencyIsoCode;

    @NotNull(message = "Deal Amount is required")
    @Positive(message = "Deal Amount must be positive")
    private Double dealAmount;

    @NotBlank(message = "Deal Timestamp is required")
    private String dealTimestamp;
}
