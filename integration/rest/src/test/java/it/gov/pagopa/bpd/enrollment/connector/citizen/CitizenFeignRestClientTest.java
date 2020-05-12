package it.gov.pagopa.bpd.enrollment.connector.citizen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import eu.sia.meda.DummyConfiguration;
import it.gov.pagopa.bpd.enrollment.connector.citizen.config.CitizenRestConnectorConfig;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@AutoConfigureWireMock
@ContextConfiguration(initializers = CitizenFeignRestClientTest.RandomPortInitializer.class)
@TestPropertySource(
        locations = "classpath:config/citizen/rest-client.properties",
        properties = "spring.application.name=bpd-ms-enrollment-integration-rest")
@Import({DummyConfiguration.class, CitizenRestConnectorConfig.class})
public class CitizenFeignRestClientTest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(
            wireMockConfig().dynamicPort()
    );

    public static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Autowired
        private Environment env;

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils
                    .addInlinedPropertiesToEnvironment(applicationContext,
                            "rest-client.citizen.base-url=" + "http://localhost:" + wireMockRule.port() + "/bpd/citizens"
                    );
        }
    }

    @Configuration
    @ImportAutoConfiguration(FeignAutoConfiguration.class)
    static class ContextConfiguration {
    }


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CitizenFeignRestClient restClient;


    @Test
    public void findById() throws JsonProcessingException {
        final String fiscalCode = "test";
        final CitizenResource expectedResponse = new CitizenResource();
        expectedResponse.setFiscalCode(fiscalCode);

        stubFor(get(urlEqualTo("/bpd/citizens/" + fiscalCode))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedResponse))));

        final CitizenResource actualResponse = restClient.findById(fiscalCode);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void update() {
    }
}