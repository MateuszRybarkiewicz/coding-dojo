package com.assignment.spring.controller;

import com.assignment.spring.CommonUtil;
import com.assignment.spring.WeatherController;
import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.configuration.PersistenceConfiguration;
import com.assignment.spring.entities.WeatherEntity;
import com.assignment.spring.properties.WeatherConnectionProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

    public static final String API_KEY = "abc";
    private static final String CITY_NAME = "foo";
    private static final String DATA_2_5_WEATHER = "/data/2.5/weather";
    private static final String DATA_2_5_QUERY_PARAMETERS = "q=%s&APPID=%s";
    private static final String PARAM_CITY_NAME = "q";
    private static final String PARAM_API_KEY = "APPID";
    @Autowired
    private WeatherController weatherController;
    @Autowired
    private WeatherConnectionProperties properties;

    @Configuration
    @EnableAutoConfiguration
    @Import({WeatherController.class, RestTemplate.class, PersistenceConfiguration.class})
    @EnableConfigurationProperties(value = WeatherConnectionProperties.class)
    public static class TestConfig {
    }


    @Test
    public void givenRequestWithValidApiKey_tIsProcessed() {
        JsonNode responseText = CommonUtil.parseTextToJson("weather.json");

        givenWiremockConfiguration(responseText, 200);

        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getParameter("city")).thenReturn("foo");

        WeatherEntity result = weatherController.weather(mock);

        WeatherResponse expectedResult = CommonUtil.map(responseText.toString(), WeatherResponse.class);

        assertResultIsCorrect(result, expectedResult);
        verifyWiremockWasCalled();
    }

    @Test
    public void givenRequestWithInvalidApiKey_unauthorizedResponseIsReturned() {
        givenWiremockConfiguration(401);

        stubFor(WireMock.get(urlPathMatching(DATA_2_5_WEATHER))
                .withQueryParam(PARAM_CITY_NAME, equalTo(CITY_NAME))
                .withQueryParam(PARAM_API_KEY, equalTo("1234"))
                .willReturn(aResponse()
                        .withStatus(401)
                ));

        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getParameter("city")).thenReturn(CITY_NAME);
        WeatherEntity result = weatherController.weather(mock);

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

    private void givenWiremockConfiguration(JsonNode responseText, int responseStatus) {
        configureWiremock(Optional.of(responseText), responseStatus);
    }

    private void givenWiremockConfiguration(int responseStatus) {
        configureWiremock(Optional.empty(), responseStatus);
    }

    private void configureWiremock(Optional<JsonNode> responseText, int responseStatus) {
        ResponseDefinitionBuilder responseBuilder = aResponse()
                .withStatus(responseStatus)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        responseText.ifPresent(r -> responseBuilder.withJsonBody(r));

        MappingBuilder mappingBuilder = WireMock.get(urlPathMatching(DATA_2_5_WEATHER))
                .withQueryParam(PARAM_CITY_NAME, equalTo(CITY_NAME))
                .withQueryParam(PARAM_API_KEY, equalTo(API_KEY))
                .willReturn(responseBuilder);
        stubFor(mappingBuilder);
    }

}
