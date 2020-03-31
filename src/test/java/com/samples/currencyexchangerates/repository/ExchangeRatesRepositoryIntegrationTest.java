package com.samples.currencyexchangerates.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.samples.currencyexchangerates.TestDbDataUtils.getTestExchangeRaeEntities;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExchangeRatesRepositoryIntegrationTest {

    public static final String EUR = "EUR";

    @Autowired
    private ExchangeRatesRepository exchangeRatesRepository;
    private List<ExchangeRateEntity> result;

    @BeforeAll
     void beforeAll() {
        exchangeRatesRepository.deleteAll();
        givenExchangesRatesInDB();
    }

    @AfterAll
    void afterAll() {
        exchangeRatesRepository.deleteAll();
    }

    @Test
    public void testExchangeRateReturnedForADay() {
        givenExchangeRateExistsForABaseAndDate();
        whenExchangeRateIsRetrievedForABaseAndDate();
        thenVerifyExchangeRatesAreRetrievedCorrectly();
    }

    @Test
    public void testExchangeRateReturnedForLastSixMonths() {
        givenExchangeRatesExistsForSixMonths();
        whenExchangeRateIsRetrievedForLastSixMonthsForABase();
        thenVerifyExchangeRatesAreRetrievedCorrectlyForSixMonths();
    }

    @Test
    public void testExchangeRateReturnedForLastNineMonths() {
        givenExchangeRatesExistsForEightMonths();
        whenExchangeRateIsRetrievedForLastNineMonthsForABase();
        thenVerifyExchangeRatesAreRetrievedCorrectlyForEightMonths();
    }

    private void givenExchangeRateExistsForABaseAndDate() {
        // data already inserted in beforeAll.Method just for readability;
    }

    private void givenExchangeRatesExistsForSixMonths() {
        // data already inserted in beforeAll.Method just for readability;
    }

    private void givenExchangeRatesExistsForEightMonths() {
        // data already inserted in beforeAll.Method just for readability;
    }

    private  void givenExchangesRatesInDB() {
        exchangeRatesRepository.saveAll(getTestExchangeRaeEntities());
    }

    private void whenExchangeRateIsRetrievedForABaseAndDate() {
        result = exchangeRatesRepository.findByBaseCurrencyAndDate(EUR, LocalDate.of(2020,3,30));
    }

    private void whenExchangeRateIsRetrievedForLastSixMonthsForABase() {
        result = exchangeRatesRepository.findByBaseCurrencyAndDateIsInOrderByDateDesc(EUR, getSameDayForLastMonths(6));
    }

    private void whenExchangeRateIsRetrievedForLastNineMonthsForABase() {
        result = exchangeRatesRepository.findByBaseCurrencyAndDateIsInOrderByDateDesc(EUR, getSameDayForLastMonths(9));
    }

    private void thenVerifyExchangeRatesAreRetrievedCorrectly() {
        assertThat(result.size()).isEqualTo(1);
    }
    private void thenVerifyExchangeRatesAreRetrievedCorrectlyForSixMonths() {
        assertThat(result.size()).isEqualTo(6);
        assertThat(result).extracting(ExchangeRateEntity::getDate).containsExactly(getSameDayForLastMonths(6).toArray(new LocalDate[]{}));
    }

    private void thenVerifyExchangeRatesAreRetrievedCorrectlyForEightMonths() {
        assertThat(result.size()).isEqualTo(8);
        assertThat(result).extracting(ExchangeRateEntity::getDate).containsExactly(getSameDayForLastMonths(8).toArray(new LocalDate[]{}));
    }

    private List<LocalDate> getSameDayForLastMonths(int num) {
        LocalDate currentDate = LocalDate.of(2020, 3, 30);
        List<LocalDate> datesToFind = new ArrayList<>();
        IntStream.range(0,num).forEach(i -> datesToFind.add(currentDate.minusMonths(i)));
        return datesToFind;
    }

}