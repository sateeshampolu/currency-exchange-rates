package com.samples.currencyexchangerates.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@RestClientTest
class RestResponseExceptionHandlerIntegrationTest {
    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;

    @Test
    public void givenRemoteApiCall_when404Error_thenThrowNotFound() {
        assertNotNull(this.server);
        RestTemplate restTemplate = this.builder
                .errorHandler(new RestResponseHandler())
                .build();

        this.server
                .expect(ExpectedCount.once(), requestTo("/test/api"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

       assertThrows(NotFoundException.class , () -> restTemplate.getForObject("/test/api", String.class));
        this.server.verify();
    }
}