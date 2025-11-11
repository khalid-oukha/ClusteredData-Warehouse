package com.datawarehouse.api.clustereddatawarehouse.services.implementation;

import com.datawarehouse.api.clustereddatawarehouse.domain.FxDeals;
import com.datawarehouse.api.clustereddatawarehouse.repository.FXDealsRepository;
import com.datawarehouse.api.clustereddatawarehouse.services.FXDealsService;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsRequestDto;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsResponseDto;
import com.datawarehouse.api.clustereddatawarehouse.web.exception.RequestAlreadyExistException;
import com.datawarehouse.api.clustereddatawarehouse.web.mapper.FXDealsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FXDealServiceImpl implements FXDealsService {
    private final FXDealsRepository fxDealsRepository;
    private final FXDealsMapper fxDealsMapper;

    @Override
    @Transactional
    public FXDealsResponseDto createDeal(final FXDealsRequestDto requestDto) {
        log.debug("Creating FX Deal with ID: {}", requestDto.getDealUniqueId());

        if (fxDealsRepository.existsById(requestDto.getDealUniqueId())) {
            log.warn("Duplicate FX Deal detected with ID: {}", requestDto.getDealUniqueId());
            throw new RequestAlreadyExistException("Deal with ID " + requestDto.getDealUniqueId() + " already exists");
        }

        FxDeals entity = fxDealsMapper.toEntity(requestDto);

        FxDeals savedEntity = fxDealsRepository.save(entity);

        log.info("FX Deal created successfully with ID: {}", savedEntity.getId());

        return fxDealsMapper.toDto(savedEntity);
    }
}
