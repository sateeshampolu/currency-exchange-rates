package com.samples.currencyexchangerates.service;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRatesService {

    /**
     * Gets latest exchange rate
     * @return
     */
    ExchangeRate getLatestExchangeRates();

    /**
     * Gets exchange rates for same given day for given no of months
     * @param date
     * @param noOfMonths
     * @return
     */
    List<ExchangeRate> getPreviousExchangeRates(LocalDate date, int noOfMonths);

    /**
     * Gets previous exchange rates for given date
     * @param date
     * @return
     */
    ExchangeRate getPreviousExchangeRate(String date);
}
