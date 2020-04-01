package com.samples.currencyexchangerates.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Component
public class RestExchangeRatesServiceImpl implements ExchangeRatesService {

    private final String latestRatesUrl;
    private final String previousRatesUrl;
    private RestTemplate restTemplate;
    public RestExchangeRatesServiceImpl(@Value("${ratesapi.latestRates:https://api.ratesapi.io/api/latest?base=EUR&symbols=GBP,USD,HKD}") String latestRatesUrl,
                                        @Value("${ratesapi.previousRates:https://api.ratesapi.io/api/{date}?base=EUR&symbols=GBP,USD,HKD}") String previousRatesUrl,
                                        RestTemplate restTemplate) {
        this.latestRatesUrl = latestRatesUrl;
        this.previousRatesUrl = previousRatesUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public ExchangeRate getLatestExchangeRates() {
        return restTemplate.getForObject(latestRatesUrl, ExchangeRate.class);
    }

    @Override
    public List<ExchangeRate> getPreviousExchangeRates(LocalDate date, int noOfMonths) {
        return getPreviousDates(date, noOfMonths).parallelStream()
                .map(previousDate -> getPreviousExchangeRate(previousDate.toString())).collect(toList());
    }

    @Cacheable("previousRates")
    @Override
    public ExchangeRate getPreviousExchangeRate(String date) {
        return restTemplate.getForObject(previousRatesUrl.replace("{date}", date.toString()), ExchangeRate.class);
    }

    // gets the same date for the given no of previous months
    private List<LocalDate> getPreviousDates(LocalDate date, int noOfMonths) {
        List<LocalDate> previousDates = new ArrayList<>();
        range(0, noOfMonths).forEach(i -> previousDates.add(date.minusMonths(i+1)));
        return previousDates;
    }
}
