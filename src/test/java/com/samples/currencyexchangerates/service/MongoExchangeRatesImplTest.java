package com.samples.currencyexchangerates.service;

import com.samples.currencyexchangerates.repository.CurrencyRate;
import com.samples.currencyexchangerates.repository.ExchangeRatesRepository;
import com.samples.currencyexchangerates.repository.ExchangeRateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MongoExchangeRatesImplTest {


    private static final String EUR = "EUR";
    private static final String USD = "USD";
    private static final String GBP = "GBP";
    private static final String HKD = "HKD";
    private static final List<String> SYMBOLS = asList(USD, GBP, HKD);
    private static int DEFAULT_NO_OF_MONTHS = 2;
    private static final CurrencyRate USD_RATE = CurrencyRate.builder().currency(USD).rate(BigDecimal.valueOf(0.87)).build();
    private static final CurrencyRate GBP_RATE = CurrencyRate.builder().currency(GBP).rate(BigDecimal.valueOf(1.02)).build();
    private static final CurrencyRate HKD_RATE = CurrencyRate.builder().currency(HKD).rate(BigDecimal.valueOf(0.12)).build();
    @Mock
    ExchangeRatesRepository exchangeRatesRepository;

    @Captor
    ArgumentCaptor<String> baseCurrencyCaptor;

    @Captor
    ArgumentCaptor<LocalDate> dateArgumentCaptor;

    @Captor
    ArgumentCaptor<List<LocalDate>> dateListArgumentCaptor;

    private LocalDate inputDate;
    private List<ExchangeRateEntity> exchangeRateEntities;
    private ExchangeRatesService exchangeRatesService;
    private List<ExchangeRate> resultExchangeRatesList;
    private ExchangeRate resultExchangeRate;

    @BeforeEach
    void setUp(){
        exchangeRatesService = new MongoExchangeRatesImpl(EUR, SYMBOLS, DEFAULT_NO_OF_MONTHS, exchangeRatesRepository);
    }

    @Test
    void testGetLatestExchangeRatesForADay() {
        givenADay();
        andGivenExchangesRatesAreReturnedForEURAndDay();
        whenGetLatestExchangeRatesForADayIsCalled();
        thenVerifyRatesAreReturnedForBaseEUROAndGivenDay();
    }

    @Test
    void testGetPreviousExchangeRates() {
        givenADay();
        andGivenPreviousExchangesRatesForGivenDay();
        whenGetPreviousExchangeRatesIsCalled();
        thenVerifyPreviousExchangeRatesAreReturned();
    }

    private void givenADay() {
        inputDate = LocalDate.now();
    }

    private void andGivenExchangesRatesAreReturnedForEURAndDay() {

        exchangeRateEntities = asList(ExchangeRateEntity.builder()
                .baseCurrency(EUR)
                .rates(unmodifiableList(asList(USD_RATE, GBP_RATE, HKD_RATE)))
                .date(LocalDate.now())
                .build());
        doReturn(exchangeRateEntities).when(exchangeRatesRepository).findByBaseCurrencyAndDate(any(), any());
    }

    private void andGivenPreviousExchangesRatesForGivenDay() {

        ExchangeRateEntity exchangeRateEntity1 = ExchangeRateEntity.builder()
                .baseCurrency(EUR)
                .rates(unmodifiableList(asList(USD_RATE, GBP_RATE, HKD_RATE)))
                .date(LocalDate.now().minusMonths(1))
                .build();
        ExchangeRateEntity exchangeRateEntity2 = ExchangeRateEntity.builder()
                .baseCurrency(EUR)
                .rates(unmodifiableList(asList(USD_RATE, GBP_RATE, HKD_RATE)))
                .date(LocalDate.now().minusMonths(2))
                .build();
        exchangeRateEntities = asList(exchangeRateEntity1, exchangeRateEntity2);
        doReturn(exchangeRateEntities).when(exchangeRatesRepository).findByBaseCurrencyAndDateIsInOrderByDateDesc(any(), any());
    }
    private void whenGetLatestExchangeRatesForADayIsCalled() {
        resultExchangeRate = exchangeRatesService.getLatestExchangeRates(inputDate);
    }


    private void whenGetPreviousExchangeRatesIsCalled() {
        resultExchangeRatesList = exchangeRatesService.getPreviousExchangeRates( inputDate);
    }

    private void thenVerifyRatesAreReturnedForBaseEUROAndGivenDay() {
        verify(exchangeRatesRepository).findByBaseCurrencyAndDate(baseCurrencyCaptor.capture(), dateArgumentCaptor.capture());
        assertThat(baseCurrencyCaptor.getValue()).isEqualTo(EUR);
        assertThat(dateArgumentCaptor.getValue()).isEqualTo(inputDate);
        assertNotNull(resultExchangeRate);
        assertThat(resultExchangeRate.getBase()).isEqualTo(EUR);
        assertThat(resultExchangeRate.getDate()).isEqualTo(inputDate);
        assertThat(resultExchangeRate.getRates()).containsOnly(entry(USD_RATE.getCurrency(), USD_RATE.getRate()),
                entry(GBP_RATE.getCurrency(), GBP_RATE.getRate()),
                entry(HKD_RATE.getCurrency(), HKD_RATE.getRate()));
    }

    private void thenVerifyPreviousExchangeRatesAreReturned() {
        verify(exchangeRatesRepository).findByBaseCurrencyAndDateIsInOrderByDateDesc(baseCurrencyCaptor.capture(),
                dateListArgumentCaptor.capture());
        assertThat(baseCurrencyCaptor.getValue()).isEqualTo(EUR);
        List<LocalDate> datesQueriedFor = dateListArgumentCaptor.getValue();
        assertThat(datesQueriedFor).containsOnly(inputDate.minusMonths(1), inputDate.minusMonths(2));
        assertThat(resultExchangeRatesList.size()).isEqualTo(2);
        ExchangeRate exchangeRate = resultExchangeRatesList.get(0);
        assertThat(exchangeRate.getBase()).isEqualTo(EUR);
        assertThat(exchangeRate.getDate()).isEqualTo(inputDate.minusMonths(1));
        assertThat(exchangeRate.getRates()).containsOnly(entry(USD_RATE.getCurrency(), USD_RATE.getRate()),
                entry(GBP_RATE.getCurrency(), GBP_RATE.getRate()),
                entry(HKD_RATE.getCurrency(), HKD_RATE.getRate()));
        exchangeRate = resultExchangeRatesList.get(1);
        assertThat(exchangeRate.getBase()).isEqualTo(EUR);
        assertThat(exchangeRate.getDate()).isEqualTo(inputDate.minusMonths(2));
        assertThat(exchangeRate.getRates()).containsOnly(entry(USD_RATE.getCurrency(), USD_RATE.getRate()),
                entry(GBP_RATE.getCurrency(), GBP_RATE.getRate()),
                entry(HKD_RATE.getCurrency(), HKD_RATE.getRate()));
    }
}