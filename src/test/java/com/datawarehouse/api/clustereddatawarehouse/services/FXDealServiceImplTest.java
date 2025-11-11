package com.datawarehouse.api.clustereddatawarehouse.services;

import com.datawarehouse.api.clustereddatawarehouse.domain.FxDeals;
import com.datawarehouse.api.clustereddatawarehouse.repository.FXDealsRepository;
import com.datawarehouse.api.clustereddatawarehouse.services.implementation.FXDealServiceImpl;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsRequestDto;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsResponseDto;
import com.datawarehouse.api.clustereddatawarehouse.web.exception.RequestAlreadyExistException;
import com.datawarehouse.api.clustereddatawarehouse.web.mapper.FXDealsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FXDealServiceImplTest {

    private FXDealsRepository fxDealsRepository;
    private FXDealsMapper fxDealsMapper;
    private FXDealServiceImpl fxDealService;

    @BeforeEach
    public void beforeEach() {
        fxDealsRepository = mock(FXDealsRepository.class);
        fxDealsMapper = mock(FXDealsMapper.class);
        fxDealService = new FXDealServiceImpl(fxDealsRepository, fxDealsMapper);
    }

    @Test
    public void createDeal_shouldCreateDeal_whenValidRequest() {
        // Arrange
        UUID dealId = UUID.randomUUID();
        FXDealsRequestDto requestDto = FXDealsRequestDto.builder()
                .dealUniqueId(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealAmount(1000.50)
                .dealTimestamp("2024-01-15T10:30:00")
                .build();

        FxDeals dealEntity = FxDeals.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealAmount(1000.50)
                .dealTimestamp(LocalDateTime.parse("2024-01-15T10:30:00"))
                .build();

        FxDeals savedDeal = FxDeals.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealAmount(1000.50)
                .dealTimestamp(LocalDateTime.parse("2024-01-15T10:30:00"))
                .createdAt(LocalDateTime.now())
                .build();

        FXDealsResponseDto responseDto = FXDealsResponseDto.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealAmount(1000.50)
                .dealTimestamp(LocalDateTime.parse("2024-01-15T10:30:00"))
                .createdAt(savedDeal.getCreatedAt())
                .build();

        when(fxDealsRepository.existsById(dealId)).thenReturn(false);
        when(fxDealsMapper.toEntity(requestDto)).thenReturn(dealEntity);
        when(fxDealsRepository.save(dealEntity)).thenReturn(savedDeal);
        when(fxDealsMapper.toDto(savedDeal)).thenReturn(responseDto);

        // Act
        FXDealsResponseDto result = fxDealService.createDeal(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(dealId, result.getId());
        assertEquals("USD", result.getOrderingCurrencyIsoCode());
        assertEquals("EUR", result.getToCurrencyIsoCode());
        assertEquals(1000.50, result.getDealAmount());
        assertNotNull(result.getCreatedAt());

        verify(fxDealsRepository, times(1)).existsById(dealId);
        verify(fxDealsMapper, times(1)).toEntity(requestDto);
        verify(fxDealsRepository, times(1)).save(dealEntity);
        verify(fxDealsMapper, times(1)).toDto(savedDeal);
    }

    @Test
    public void createDeal_shouldThrowRequestAlreadyExistException_whenDealIdExists() {
        // Arrange
        UUID dealId = UUID.randomUUID();
        FXDealsRequestDto requestDto = FXDealsRequestDto.builder()
                .dealUniqueId(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealAmount(1000.50)
                .dealTimestamp("2024-01-15T10:30:00")
                .build();

        when(fxDealsRepository.existsById(dealId)).thenReturn(true);

        // Act & Assert
        RequestAlreadyExistException exception = assertThrows(
                RequestAlreadyExistException.class,
                () -> fxDealService.createDeal(requestDto),
                "Should throw RequestAlreadyExistException when deal ID already exists"
        );

        assertEquals("Deal with ID " + dealId + " already exists", exception.getMessage());
        verify(fxDealsRepository, times(1)).existsById(dealId);
        verify(fxDealsMapper, never()).toEntity(any(FXDealsRequestDto.class));
        verify(fxDealsRepository, never()).save(any(FxDeals.class));
        verify(fxDealsMapper, never()).toDto(any(FxDeals.class));
    }

    @Test
    public void createDeal_shouldHandleMultipleCurrencies() {
        // Arrange
        UUID dealId = UUID.randomUUID();
        FXDealsRequestDto requestDto = FXDealsRequestDto.builder()
                .dealUniqueId(dealId)
                .orderingCurrencyIsoCode("GBP")
                .toCurrencyIsoCode("JPY")
                .dealAmount(5000.75)
                .dealTimestamp("2024-01-20T14:25:00")
                .build();

        FxDeals dealEntity = FxDeals.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("GBP")
                .toCurrencyIsoCode("JPY")
                .dealAmount(5000.75)
                .dealTimestamp(LocalDateTime.parse("2024-01-20T14:25:00"))
                .build();

        FxDeals savedDeal = FxDeals.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("GBP")
                .toCurrencyIsoCode("JPY")
                .dealAmount(5000.75)
                .dealTimestamp(LocalDateTime.parse("2024-01-20T14:25:00"))
                .createdAt(LocalDateTime.now())
                .build();

        FXDealsResponseDto responseDto = FXDealsResponseDto.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("GBP")
                .toCurrencyIsoCode("JPY")
                .dealAmount(5000.75)
                .dealTimestamp(LocalDateTime.parse("2024-01-20T14:25:00"))
                .createdAt(savedDeal.getCreatedAt())
                .build();

        when(fxDealsRepository.existsById(dealId)).thenReturn(false);
        when(fxDealsMapper.toEntity(requestDto)).thenReturn(dealEntity);
        when(fxDealsRepository.save(dealEntity)).thenReturn(savedDeal);
        when(fxDealsMapper.toDto(savedDeal)).thenReturn(responseDto);

        // Act
        FXDealsResponseDto result = fxDealService.createDeal(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("GBP", result.getOrderingCurrencyIsoCode());
        assertEquals("JPY", result.getToCurrencyIsoCode());
        assertEquals(5000.75, result.getDealAmount());

        verify(fxDealsRepository, times(1)).existsById(dealId);
        verify(fxDealsRepository, times(1)).save(dealEntity);
    }

    @Test
    public void createDeal_shouldHandleLargeAmounts() {
        // Arrange
        UUID dealId = UUID.randomUUID();
        Double largeAmount = 999999.99;
        FXDealsRequestDto requestDto = FXDealsRequestDto.builder()
                .dealUniqueId(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("CHF")
                .dealAmount(largeAmount)
                .dealTimestamp("2024-01-22T16:45:00")
                .build();

        FxDeals dealEntity = FxDeals.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("CHF")
                .dealAmount(largeAmount)
                .dealTimestamp(LocalDateTime.parse("2024-01-22T16:45:00"))
                .build();

        FxDeals savedDeal = FxDeals.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("CHF")
                .dealAmount(largeAmount)
                .dealTimestamp(LocalDateTime.parse("2024-01-22T16:45:00"))
                .createdAt(LocalDateTime.now())
                .build();

        FXDealsResponseDto responseDto = FXDealsResponseDto.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("CHF")
                .dealAmount(largeAmount)
                .dealTimestamp(LocalDateTime.parse("2024-01-22T16:45:00"))
                .createdAt(savedDeal.getCreatedAt())
                .build();

        when(fxDealsRepository.existsById(dealId)).thenReturn(false);
        when(fxDealsMapper.toEntity(requestDto)).thenReturn(dealEntity);
        when(fxDealsRepository.save(dealEntity)).thenReturn(savedDeal);
        when(fxDealsMapper.toDto(savedDeal)).thenReturn(responseDto);

        // Act
        FXDealsResponseDto result = fxDealService.createDeal(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(largeAmount, result.getDealAmount());
        verify(fxDealsRepository, times(1)).save(dealEntity);
    }
}
