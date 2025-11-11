package com.datawarehouse.api.clustereddatawarehouse.controller;

import com.datawarehouse.api.clustereddatawarehouse.services.FXDealsService;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsRequestDto;
import com.datawarehouse.api.clustereddatawarehouse.web.dto.fxDeals.FXDealsResponseDto;
import com.datawarehouse.api.clustereddatawarehouse.web.rest.FXDealController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FXDealControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FXDealsService fxDealsService;

    @InjectMocks
    private FXDealController fxDealController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fxDealController)
                .build();
    }

    @Test
    public void createDeal_shouldReturnCreated_whenValidRequest() throws Exception {
        // Arrange
        UUID dealId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        FXDealsRequestDto requestDto = FXDealsRequestDto.builder()
                .dealUniqueId(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealAmount(1000.50)
                .dealTimestamp("2024-01-15T10:30:00")
                .build();

        FXDealsResponseDto responseDto = FXDealsResponseDto.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealAmount(1000.50)
                .dealTimestamp(LocalDateTime.parse("2024-01-15T10:30:00"))
                .createdAt(LocalDateTime.now())
                .build();

        when(fxDealsService.createDeal(any(FXDealsRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("123e4567-e89b-12d3-a456-426614174000")))
                .andExpect(jsonPath("$.orderingCurrencyIsoCode", is("USD")))
                .andExpect(jsonPath("$.toCurrencyIsoCode", is("EUR")))
                .andExpect(jsonPath("$.dealAmount", is(1000.50)))
                .andExpect(jsonPath("$.dealTimestamp", notNullValue()))
                .andExpect(jsonPath("$.createdAt", notNullValue()));

        verify(fxDealsService, times(1)).createDeal(any(FXDealsRequestDto.class));
    }


    @Test
    public void createDeal_shouldReturnBadRequest_whenMissingDealUniqueId() throws Exception {
        // Arrange
        String invalidRequest = """
                {
                    "orderingCurrencyIsoCode": "USD",
                    "toCurrencyIsoCode": "EUR",
                    "dealAmount": 1000.50,
                    "dealTimestamp": "2024-01-15T10:30:00"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(fxDealsService, never()).createDeal(any(FXDealsRequestDto.class));
    }

    @Test
    public void createDeal_shouldReturnBadRequest_whenInvalidCurrencyCode() throws Exception {
        // Arrange
        UUID dealId = UUID.randomUUID();
        String invalidRequest = String.format("""
                {
                    "dealUniqueId": "%s",
                    "orderingCurrencyIsoCode": "us",
                    "toCurrencyIsoCode": "EUR",
                    "dealAmount": 1000.50,
                    "dealTimestamp": "2024-01-15T10:30:00"
                }
                """, dealId);

        // Act & Assert
        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(fxDealsService, never()).createDeal(any(FXDealsRequestDto.class));
    }

    @Test
    public void createDeal_shouldReturnBadRequest_whenNegativeAmount() throws Exception {
        // Arrange
        UUID dealId = UUID.randomUUID();
        String invalidRequest = String.format("""
                {
                    "dealUniqueId": "%s",
                    "orderingCurrencyIsoCode": "USD",
                    "toCurrencyIsoCode": "EUR",
                    "dealAmount": -1000.50,
                    "dealTimestamp": "2024-01-15T10:30:00"
                }
                """, dealId);

        // Act & Assert
        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(fxDealsService, never()).createDeal(any(FXDealsRequestDto.class));
    }

    @Test
    public void createDeal_shouldReturnBadRequest_whenMissingTimestamp() throws Exception {
        // Arrange
        UUID dealId = UUID.randomUUID();
        String invalidRequest = String.format("""
                {
                    "dealUniqueId": "%s",
                    "orderingCurrencyIsoCode": "USD",
                    "toCurrencyIsoCode": "EUR",
                    "dealAmount": 1000.50
                }
                """, dealId);

        // Act & Assert
        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(fxDealsService, never()).createDeal(any(FXDealsRequestDto.class));
    }

    @Test
    public void createDeal_shouldAcceptDifferentCurrencyPairs() throws Exception {
        // Arrange
        UUID dealId = UUID.randomUUID();
        FXDealsRequestDto requestDto = FXDealsRequestDto.builder()
                .dealUniqueId(dealId)
                .orderingCurrencyIsoCode("GBP")
                .toCurrencyIsoCode("JPY")
                .dealAmount(5000.75)
                .dealTimestamp("2024-01-20T14:25:00")
                .build();

        FXDealsResponseDto responseDto = FXDealsResponseDto.builder()
                .id(dealId)
                .orderingCurrencyIsoCode("GBP")
                .toCurrencyIsoCode("JPY")
                .dealAmount(5000.75)
                .dealTimestamp(LocalDateTime.parse("2024-01-20T14:25:00"))
                .createdAt(LocalDateTime.now())
                .build();

        when(fxDealsService.createDeal(any(FXDealsRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderingCurrencyIsoCode", is("GBP")))
                .andExpect(jsonPath("$.toCurrencyIsoCode", is("JPY")))
                .andExpect(jsonPath("$.dealAmount", is(5000.75)));

        verify(fxDealsService, times(1)).createDeal(any(FXDealsRequestDto.class));
    }

    @Test
    public void createDeal_shouldReturnBadRequest_whenInvalidCurrencyLength() throws Exception {
        // Arrange
        UUID dealId = UUID.randomUUID();
        String invalidRequest = String.format("""
                {
                    "dealUniqueId": "%s",
                    "orderingCurrencyIsoCode": "US",
                    "toCurrencyIsoCode": "EURO",
                    "dealAmount": 1000.50,
                    "dealTimestamp": "2024-01-15T10:30:00"
                }
                """, dealId);

        // Act & Assert
        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(fxDealsService, never()).createDeal(any(FXDealsRequestDto.class));
    }
}
