package com.datawarehouse.api.clustereddatawarehouse.web.mapper;

import com.datawarehouse.api.clustereddatawarehouse.domain.FxDeals;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsRequestDto;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface FXDealsMapper {

    @Mapping(target = "id", source = "dealUniqueId")
    @Mapping(target = "dealTimestamp", expression = "java(parseTimestamp(dto.getDealTimestamp()))")
    @Mapping(target = "createdAt", ignore = true)
    FxDeals toEntity(FXDealsRequestDto dto);

    FXDealsResponseDto toDto(FxDeals entity);

    default LocalDateTime parseTimestamp(String timestamp) {
        try {
            return LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid timestamp format. Expected ISO format (e.g., 2024-01-15T10:30:00)");
        }
    }
}

