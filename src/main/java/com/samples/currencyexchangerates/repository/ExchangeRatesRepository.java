package com.samples.currencyexchangerates.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRatesRepository extends MongoRepository<ExchangeRateEntity, String> {
     List<ExchangeRateEntity> findByBaseCurrencyAndDate(String baseCurrency, LocalDate date);
     List<ExchangeRateEntity> findByBaseCurrencyAndDateIsInOrderByDateDesc(String baseCurrency, List<LocalDate> date);
}

