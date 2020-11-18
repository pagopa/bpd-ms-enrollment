package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import it.gov.pagopa.bpd.common.connector.BaseFeignRestClientTest;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.config.PaymentInstrumentRestConnectorConfig;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@TestPropertySource(
        locations = "classpath:config/payment_instrument/rest-client.properties",
        properties = {
                "spring.application.name=bpd-ms-enrollment-integration-rest",
                "feign.client.config.bpd-ms-payment-instrument.connectTimeout=10000",
                "feign.client.config.bpd-ms-payment-instrument.readTimeout=10000",
                "logging.level.it.gov.pagopa.bpd.enrollment=DEBUG"
        })
@ContextConfiguration(initializers = PaymentInstrumentRestClientImplTest.RandomPortInitializer.class,
        classes = PaymentInstrumentRestConnectorConfig.class)
public class PaymentInstrumentRestClientImplTest extends BaseFeignRestClientTest {

    @ClassRule
    public static WireMockClassRule wireMockRule;

    static {
        String port = System.getenv("WIREMOCKPORT");
        wireMockRule = new WireMockClassRule(wireMockConfig()
                .port(port != null ? Integer.parseInt(port) : 0)
                .bindAddress("localhost")
                .usingFilesUnderClasspath("stubs/payment-instrument")
                .extensions(new ResponseTemplateTransformer(false))
        );
    }

    @Test
    public void update() {
        PaymentInstrumentDto request = new PaymentInstrumentDto();
        request.setActivationDate(OffsetDateTime.parse("2020-04-17T12:23:00.749+02:00"));
        request.setFiscalCode("fiscalCode");

        final String hpan = "hpan";
        final PaymentInstrumentResource actualResponse = restClient.update(request, hpan);

        assertNotNull(actualResponse);
        assertEquals(hpan, actualResponse.getHpan());
        assertEquals(request.getFiscalCode(), actualResponse.getFiscalCode());
        assertEquals(request.getActivationDate(), actualResponse.getActivationDate());
    }

    @Test
    public void deleteByFiscalCode() {
        restClient.deleteByFiscalCode("test", "channel");
    }

    @Test
    public void rollback() {
        restClient.rollback("test", OffsetDateTime.now());
    }


    @Autowired
    private PaymentInstrumentRestClient restClient;

    public static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils
                    .addInlinedPropertiesToEnvironment(applicationContext,
                            String.format("rest-client.payment-instrument.base-url=http://%s:%d/bpd/payment-instruments",
                                    wireMockRule.getOptions().bindAddress(),
                                    wireMockRule.port())
                    );
        }
    }
}