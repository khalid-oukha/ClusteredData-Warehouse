package com.datawarehouse.api.clustereddatawarehouse.services;

import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsRequestDto;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsResponseDto;

public interface FXDealsService {
    FXDealsResponseDto createDeal(FXDealsRequestDto requestDto);
}

