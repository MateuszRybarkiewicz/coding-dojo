package com.assignment.spring.controller;

import com.assignment.spring.CommonUtil;
import com.assignment.spring.WeatherController;
import com.assignment.spring.api.WeatherResponse;
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
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WeatherControllerITest.TestConfig.class)
@WireMockTest(httpPort = 1234)
@SetEnvironmentVariable(key = "WEATHER_APIKEY", value = WeatherControllerITest.API_KEY)
public class WeatherControllerITest {

    public static final String API_KEY = "1234";
    public static final String CITY_NAME = "foo";
    public static final String DATA_2_5_WEATHER = "/data/2.5/weather";
    public static final String DATA_2_5_QUERY_PARAMETERS = "q=%s&APPID=%s";
    public static final String PARAM_CITY_NAME = "q";
    public static final String PARAM_API_KEY = "APPID";
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

        stubFor(WireMock.get(urlPathMatching(DATA_2_5_WEATHER))
                .withQueryParam(PARAM_CITY_NAME, equalTo(CITY_NAME))
                .withQueryParam(PARAM_API_KEY, equalTo(API_KEY))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withJsonBody(responseText)
                        .withHeader("Content-Type", "application/json")
                ));

        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getParameter("city")).thenReturn(CITY_NAME);
        WeatherEntity result = weatherController.weather(mock);

        WeatherResponse expectedResult = CommonUtil.map(responseText.toString(), WeatherResponse.class);
        assertResultIsCorrect(result, expectedResult);
        verifyWiremockWasCalled();
    }

    private void verifyWiremockWasCalled() {
        verify(getRequestedFor(urlEqualTo(DATA_2_5_WEATHER + "?" + String.format(DATA_2_5_QUERY_PARAMETERS, CITY_NAME, API_KEY))));
    }


    private void assertResultIsCorrect(WeatherEntity result, WeatherResponse expectedResult) {
        assertEquals(expectedResult.getSys().getCountry(), result.getCountry());
        assertEquals(expectedResult.getName(), result.getCity());
        assertEquals(expectedResult.getMain().getTemp(), result.getTemperature());
    }
}
