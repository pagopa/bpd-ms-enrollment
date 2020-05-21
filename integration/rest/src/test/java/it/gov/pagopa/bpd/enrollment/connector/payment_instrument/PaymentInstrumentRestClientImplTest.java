package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.bpd.common.connector.BaseFeignRestClientTest;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.config.PaymentInstrumentRestConnectorConfig;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

@TestPropertySource(
        locations = "classpath:config/payment_instrument/rest-client.properties",
        properties = "spring.application.name=bpd-ms-enrollment-integration-rest")
@Import({PaymentInstrumentRestConnectorConfig.class})
public class PaymentInstrumentRestClientImplTest extends BaseFeignRestClientTest {

    static {
        // set with the env var name related to rest client service port
        SERIVICE_PORT_ENV_VAR_NAME = "BPD_PAYMENT_INSTRUMENT_PORT";
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentInstrumentRestClient restClient;

    private HttpStatus httpStatus;

    @Test
    public void update() throws IOException {
        final String hpan = "hpan1";
        final String fiscalCode = "fiscalCode";

        InputStream mockedJson = getClass()
                .getClassLoader()
                .getResourceAsStream(format("payment_instrument/paymentInstrument_%s.json", hpan));

        final JsonNode jsonNode = objectMapper.readTree(mockedJson);

        stubFor(put(urlEqualTo("/bpd/payment-instruments/" + hpan))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(jsonNode.toString())));


        PaymentInstrumentDto request = new PaymentInstrumentDto();
        request.setActivationDate(OffsetDateTime.now());
        request.setFiscalCode(fiscalCode);
        final PaymentInstrumentResource actualResponse = restClient.update(request, hpan);

        assertEquals(jsonNode, objectMapper.valueToTree(actualResponse));

    }
}