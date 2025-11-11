package com.datawarehouse.api.clustereddatawarehouse.web.rest;

import com.datawarehouse.api.clustereddatawarehouse.services.FXDealsService;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsRequestDto;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fx-deals")
@RequiredArgsConstructor
@Slf4j
public class FXDealController {

    private final FXDealsService fxDealsService;

    @PostMapping
    public ResponseEntity<FXDealsResponseDto> createDeal(@Valid @RequestBody FXDealsRequestDto requestDto) {
        log.info("Received request to create FX Deal with ID: {}", requestDto.getDealUniqueId());
        FXDealsResponseDto response = fxDealsService.createDeal(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}