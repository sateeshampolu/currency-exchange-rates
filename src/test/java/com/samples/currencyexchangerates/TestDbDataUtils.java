package com.samples.currencyexchangerates;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.samples.currencyexchangerates.repository.ExchangeRateEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDbDataUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static List<ExchangeRateEntity> getTestExchangeRaeEntities() {
        CollectionType javaType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ExchangeRateEntity.class);
        try {
            return objectMapper.readValue(readFromFile(), javaType);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static String readFromFile() throws IOException, URISyntaxException {
        Path path = Paths.get(TestDbDataUtils.class.getClassLoader()
                .getResource("exchangeRatesTest.json").toURI());
        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        return data;
    }

}
