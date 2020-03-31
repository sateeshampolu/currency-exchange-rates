package com.samples.currencyexchangerates;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class CurrencyExchangeRatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeRatesApplication.class, args);
    }
}
