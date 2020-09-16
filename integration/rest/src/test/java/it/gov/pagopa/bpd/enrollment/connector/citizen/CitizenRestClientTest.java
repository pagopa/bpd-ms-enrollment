package it.gov.pagopa.bpd.enrollment.connector.citizen;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import it.gov.pagopa.bpd.common.connector.BaseFeignRestClientTest;
import it.gov.pagopa.bpd.enrollment.connector.citizen.config.CitizenRestConnectorConfig;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import lombok.SneakyThrows;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@TestPropertySource(
        locations = "classpath:config/citizen/rest-client.properties",
        properties = {
                "logging.level.it.gov.pagopa.bpd.enrollment=DEBUG",
                "spring.application.name=bpd-ms-enrollment-integration-rest"
        })
@ContextConfiguration(initializers = CitizenRestClientTest.RandomPortInitializer.class,
        classes = CitizenRestConnectorConfig.class)
public class CitizenRestClientTest extends BaseFeignRestClientTest {

    @ClassRule
    public static WireMockClassRule wireMockRule;

    static {
        String port = System.getenv("WiremockPort");
        wireMockRule = new WireMockClassRule(wireMockConfig()
                .port(port != null ? Integer.parseInt(port) : 0)
                .bindAddress("localhost")
                .usingFilesUnderClasspath("stubs/citizen")
                .extensions(new ResponseTemplateTransformer(false))
        );
    }

    @Test
    public void findById() {
        final String fiscalCode = "fiscalCode";

        final CitizenResource actualResponse = restClient.findById(fiscalCode);

        assertNotNull(actualResponse);
        assertEquals(fiscalCode, actualResponse.getFiscalCode());
    }

    @Autowired
    private CitizenRestClient restClient;

    @Test
    public void update() {
        final String fiscalCode = "fiscalCode";
        CitizenDto request = new CitizenDto();
        request.setTimestampTC(OffsetDateTime.parse("2020-04-17T12:23:00.749+00:00"));

        final CitizenResource actualResponse = restClient.update(fiscalCode, request);

        assertNotNull(actualResponse);
        assertEquals(fiscalCode, actualResponse.getFiscalCode());
    }

    @Test
    public void delete() {
        restClient.delete("test");
    }

    public static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils
                    .addInlinedPropertiesToEnvironment(applicationContext,
                            String.format("rest-client.citizen.base-url=http://%s:%d/bpd/citizens",
                                    wireMockRule.getOptions().bindAddress(),
                                    wireMockRule.port())
                    );
        }
    }

}