package com.samples.currencyexchangerates.web.rest.controller;

import com.samples.currencyexchangerates.service.ExchangeRate;
import com.samples.currencyexchangerates.service.ExchangeRatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesRestControllerTest {
    public static final int DEFAULT_NO_OF_MONTHS = 2;
    @Mock
    private ExchangeRate exchangeRate;
    @Mock
    private ExchangeRatesService exchangeRatesService;

    private ExchangeRatesRestController exchangeRatesRestController;
    private ResponseEntity<ExchangeRate> resultExchangeRate;
    private ResponseEntity<List<ExchangeRate>> resultPreviousExchangeRates;

    @BeforeEach
    void setUp() {
        exchangeRatesRestController = new ExchangeRatesRestController(DEFAULT_NO_OF_MONTHS, exchangeRatesService);
    }

    @Test
    void testGetLatestExchangeRates() {
        givenExchangeRatesForToday();
        whenGetLatestExchangeRates();
        thenVerifyExchangeRatesServiceIsCalled();
        andVerifyResponseIsReturned();
    }

    @Test
    void testGetPreviousExchangeRates() {
        givenPreviousExchangeRates();
        whenGetPreviousExchangeRates();
        thenVerifyExchangeRatesServiceIsCalledForPreviousExchangeRates();
        andVerifyResponseIsReturnedForPreviousExchangeRates();
    }

    private void givenExchangeRatesForToday() {
        when(exchangeRatesService.getLatestExchangeRates()).thenReturn(exchangeRate);
    }

    private void givenPreviousExchangeRates() {
        when(exchangeRatesService.getPreviousExchangeRates(LocalDate.now(), DEFAULT_NO_OF_MONTHS)).thenReturn(asList(exchangeRate));
    }

    private void whenGetLatestExchangeRates() {
        resultExchangeRate = exchangeRatesRestController.getLatestExchangeRates();
    }

    private void whenGetPreviousExchangeRates() {
        resultPreviousExchangeRates = exchangeRatesRestController.getPreviousExchangeRates(LocalDate.now());
    }

    private void thenVerifyExchangeRatesServiceIsCalled() {
        verify(exchangeRatesService).getLatestExchangeRates();
    }

    private void thenVerifyExchangeRatesServiceIsCalledForPreviousExchangeRates() {
        verify(exchangeRatesService).getPreviousExchangeRates(LocalDate.now(), DEFAULT_NO_OF_MONTHS);
    }

    private void andVerifyResponseIsReturned() {
        assertThat(resultExchangeRate.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultExchangeRate.getBody()).isEqualTo(exchangeRate);
    }

    private void andVerifyResponseIsReturnedForPreviousExchangeRates() {
        assertThat(resultPreviousExchangeRates.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultPreviousExchangeRates.getBody()).isEqualTo(Arrays.asList(exchangeRate));
    }
}