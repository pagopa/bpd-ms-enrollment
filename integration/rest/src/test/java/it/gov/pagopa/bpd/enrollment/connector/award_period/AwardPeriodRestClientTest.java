package it.gov.pagopa.bpd.enrollment.connector.award_period;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import it.gov.pagopa.bpd.common.connector.BaseFeignRestClientTest;
import it.gov.pagopa.bpd.enrollment.connector.award_period.config.AwardPeriodRestConnectorConfig;
import it.gov.pagopa.bpd.enrollment.connector.award_period.model.AwardPeriodResource;
import lombok.SneakyThrows;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertNotNull;

@TestPropertySource(
        locations = "classpath:config/award_period/rest-client.properties",
        properties = {
                "logging.level.it.gov.pagopa.bpd.enrollment=DEBUG",
                "spring.application.name=bpd-ms-enrollment-integration-rest"
        })
@ContextConfiguration(initializers = AwardPeriodRestClientTest.RandomPortInitializer.class,
        classes = AwardPeriodRestConnectorConfig.class)
public class AwardPeriodRestClientTest extends BaseFeignRestClientTest {


    @ClassRule
    public static WireMockClassRule wireMockRule;

    static {
        String port = System.getenv("WIREMOCKPORT");
        wireMockRule = new WireMockClassRule(wireMockConfig()
                .port(port != null ? Integer.parseInt(port) : 0)
                .bindAddress("localhost")
                .usingFilesUnderClasspath("stubs/award-period")
                .extensions(new ResponseTemplateTransformer(false))
        );
    }

    @Test
    public void findActives() {

        final List<AwardPeriodResource> actualResponse = restClient.findActives();
        assertNotNull(actualResponse);
    }

    @Autowired
    private AwardPeriodRestClient restClient;


    public static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils
                    .addInlinedPropertiesToEnvironment(applicationContext,
                            String.format("rest-client.award-period.base-url=http://%s:%d/bpd/award-periods",
                                    wireMockRule.getOptions().bindAddress(),
                                    wireMockRule.port())
                    );
        }
    }


}