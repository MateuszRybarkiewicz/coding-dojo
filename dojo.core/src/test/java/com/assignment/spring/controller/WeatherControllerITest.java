package com.assignment.spring.controller;

import com.assignment.spring.CommonUtil;
import com.assignment.spring.WeatherController;
import com.assignment.spring.configuration.PersistenceConfiguration;
import com.assignment.spring.entities.WeatherEntity;
import com.assignment.spring.properties.WeatherConnectionProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

@SpringBootTest(classes = WeatherControllerITest.TestConfig.class)
@WireMockTest(httpPort = 1234)
@SetEnvironmentVariable(key = "WEATHER_APIKEY", value = "123")
public class WeatherControllerITest {

    @Autowired
    private WeatherController weatherController;

    @Configuration
    @EnableAutoConfiguration
    @Import({WeatherController.class, RestTemplate.class, PersistenceConfiguration.class})
    @EnableConfigurationProperties(value = WeatherConnectionProperties.class)
    public static class TestConfig {
    }

    @Test
    public void givenRequestWithValidApiKey_ItIsProcessed() {
        JsonNode responseText = CommonUtil.parseTextToJson("weather.json");
        stubFor(WireMock.get(urlPathMatching(".*"))
                .withQueryParam("APPID", equalTo("123"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withJsonBody(responseText)
                        .withHeader("Content-Type", "application/json")
                ));

        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getParameter("city")).thenReturn("foo");
        WeatherEntity weather = weatherController.weather(mock);

    }
}
