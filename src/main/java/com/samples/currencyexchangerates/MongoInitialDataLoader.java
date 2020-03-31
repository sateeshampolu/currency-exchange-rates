package com.samples.currencyexchangerates;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.samples.currencyexchangerates.repository.ExchangeRateEntity;
import com.samples.currencyexchangerates.repository.ExchangeRatesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class MongoInitialDataLoader implements CommandLineRunner {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRatesRepository exchangeRatesRepository;
    private final String dataFileName;

    public MongoInitialDataLoader(@Value("${dataFileName:exchangeRates.json}") String dataFileName, ExchangeRatesRepository exchangeRatesRepository) {
        this.exchangeRatesRepository = exchangeRatesRepository;
        this.dataFileName = dataFileName;
    }

    public void loadInitialData() throws IOException, URISyntaxException {
        CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, ExchangeRateEntity.class);
        Iterable<ExchangeRateEntity> entities = objectMapper.readValue(readFromFile(), javaType);
        exchangeRatesRepository.saveAll(updateDates(entities));
    }

    private List<ExchangeRateEntity> updateDates(Iterable<ExchangeRateEntity> entities) {
        int count = 0;
        List<ExchangeRateEntity> exchangeRateEntities = new ArrayList<>();
        Iterator<ExchangeRateEntity> iterator = entities.iterator();
        LocalDate currentDate = LocalDate.now();
        while (iterator.hasNext()) {
            if (count == 0) {
                exchangeRateEntities.add(iterator.next().toBuilder().date(currentDate).build());
            } else {
                exchangeRateEntities.add(iterator.next().toBuilder().date(currentDate.minusMonths(count)).build());
            }
            count++;
        }
        return exchangeRateEntities;
    }

    private String readFromFile() throws IOException, URISyntaxException {
        Path path = Paths.get(this.getClass().getClassLoader()
                .getResource(dataFileName).toURI());
        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        return data;
    }

    @Override
    public void run(String... args) throws Exception {
        loadInitialData();
    }
}
