package it.gov.pagopa.bpd.enrollment.connector.winning_transaction;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import it.gov.pagopa.bpd.common.connector.BaseFeignRestClientTest;
import it.gov.pagopa.bpd.enrollment.connector.winning_transaction.config.WinningTransactionConnectorConfig;
import lombok.SneakyThrows;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@TestPropertySource(
        locations = "classpath:config/winning_transaction/rest-client.properties",
        properties = {
                "spring.application.name=bpd-ms-enrollment-integration-rest",
                "feign.client.config.bpd-ms-winning-transaction.connectTimeout=10000",
                "feign.client.config.bpd-ms-winning-transaction.readTimeout=10000",
                "logging.level.it.gov.pagopa.bpd.enrollment=DEBUG"
        })
@ContextConfiguration(initializers = it.gov.pagopa.bpd.enrollment.connector.winning_transaction.WinningTransactionRestClientTest.RandomPortInitializer.class,
        classes = WinningTransactionConnectorConfig.class)
public class WinningTransactionRestClientTest extends BaseFeignRestClientTest {

    @ClassRule
    public static WireMockClassRule wireMockRule;

    static {
        String port = System.getenv("WIREMOCKPORT");
        wireMockRule = new WireMockClassRule(wireMockConfig()
                .port(port != null ? Integer.parseInt(port) : 0)
                .bindAddress("localhost")
                .usingFilesUnderClasspath("stubs/winning-transaction")
                .extensions(new ResponseTemplateTransformer(false))
        );
    }

    @Test
    public void deleteByFiscalCode() {
        restClient.deleteByFiscalCode("test");
    }


    @Autowired
    private WinningTransactionRestClient restClient;

    public static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils
                    .addInlinedPropertiesToEnvironment(applicationContext,
                            String.format("rest-client.winning-transaction.base-url=http://%s:%d/bpd/winning-transactions",
                                    wireMockRule.getOptions().bindAddress(),
                                    wireMockRule.port())
                    );
        }
    }

}