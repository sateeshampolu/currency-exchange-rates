package com.samples.currencyexchangerates.service;

import com.samples.currencyexchangerates.exceptions.NotFoundException;
import com.samples.currencyexchangerates.repository.CurrencyRate;
import com.samples.currencyexchangerates.repository.ExchangeRateEntity;
import com.samples.currencyexchangerates.repository.ExchangeRatesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class MongoExchangeRatesImpl implements ExchangeRatesService {

    private final String defaultBaseCurrency;
    private final List<String> defaultSymbols;
    private final ExchangeRatesRepository exchangeRatesRepository;

    public MongoExchangeRatesImpl(@Value("${defaultBaseCurrency:EUR}") String defaultBaseCurrency,
                                  @Value("#{'${defaultBaseCurrency:GBP,USD,HKD}'.split(',')}") List<String> symbols,
                                  ExchangeRatesRepository exchangeRatesRepository) {
        this.defaultBaseCurrency = defaultBaseCurrency;
        this.exchangeRatesRepository = exchangeRatesRepository;
        this.defaultSymbols = symbols;
    }

    @Override
    public ExchangeRate getLatestExchangeRates() {
        List<ExchangeRate> exchangeRates = getLatestExchangeRates(defaultBaseCurrency, LocalDate.now(), defaultSymbols);
        return exchangeRates.get(0);
    }

    private List<ExchangeRate> getLatestExchangeRates(String baseCurrency, LocalDate date, List<String> symbols) {

        List<ExchangeRateEntity> result = exchangeRatesRepository.findByBaseCurrencyAndDate(
                baseCurrency, date);
        if (isNotEmpty(result)) {
            return result.stream().map(exchangeRateEntity -> mapFrom(exchangeRateEntity, symbols)).collect(Collectors.toList());
        }
        throw new NotFoundException(String.format("Exchange rate not found"));
    }

    @Override
    public List<ExchangeRate> getPreviousExchangeRates(LocalDate date, int noOfMonths) {
        return getPreviousExchangeRates(defaultBaseCurrency, date, noOfMonths, defaultSymbols);
    }

    @Override
    public ExchangeRate getPreviousExchangeRate(String date) {
        return null;
    }


    private List<ExchangeRate> getPreviousExchangeRates(String baseCurrency, LocalDate date, int noOfMonths, List<String> symbols) {
        List<ExchangeRateEntity> result = exchangeRatesRepository.findByBaseCurrencyAndDateIsInOrderByDateDesc(baseCurrency, getPreviousDates(date, noOfMonths));

        if (isNotEmpty(result)) {
            return result.stream().map(exchangeRateEntity -> mapFrom(exchangeRateEntity, symbols)).collect(Collectors.toList());
        }
        throw new NotFoundException(String.format("Exchange rate not found"));
    }

    private ExchangeRate mapFrom(ExchangeRateEntity exchangeRateEntity, List<String> symbols) {
        List<CurrencyRate> currenciesToReturn = exchangeRateEntity.getRates().stream().filter(currencyRate -> symbols.contains(currencyRate.getCurrency())).collect(Collectors.toList());
        return ExchangeRate.builder().base(exchangeRateEntity.getBaseCurrency())
                .date(exchangeRateEntity.getDate())
                .rates(currenciesToReturn.stream().collect(Collectors.toMap(CurrencyRate::getCurrency, CurrencyRate::getRate)))
                .build();
    }

    // gets the same date for the given no of previous months
    private List<LocalDate> getPreviousDates(LocalDate date, int noOfMonths) {
        List<LocalDate> previousDates = new ArrayList<>();
        range(0, noOfMonths).forEach(i -> previousDates.add(date.minusMonths(i+1)));
        return previousDates;
    }
}
