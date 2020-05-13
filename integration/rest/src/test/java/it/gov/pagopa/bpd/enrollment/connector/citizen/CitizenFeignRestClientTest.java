package it.gov.pagopa.bpd.enrollment.connector.citizen;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.bpd.common.connector.BaseFeignRestClientTest;
import it.gov.pagopa.bpd.enrollment.connector.citizen.config.CitizenRestConnectorConfig;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

@TestPropertySource(
        locations = "classpath:config/citizen/rest-client.properties",
        properties = "spring.application.name=bpd-ms-enrollment-integration-rest")
@Import({CitizenRestConnectorConfig.class})
public class CitizenFeignRestClientTest extends BaseFeignRestClientTest {

    static {
        // set with the env var name related to rest client service port
        SERIVICE_PORT_ENV_VAR_NAME = "BPD_CITIZEN_PORT";
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CitizenFeignRestClient restClient;


    @Test
    public void findById() throws IOException {
        final String fiscalCode = "fiscalCode1";

        InputStream mockedJson = getClass()
                .getClassLoader()
                .getResourceAsStream(format("citizen/citizen_%s.json", fiscalCode));

        final JsonNode jsonNode = objectMapper.readTree(mockedJson);

        stubFor(get(urlEqualTo("/bpd/citizens/" + fiscalCode))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(jsonNode.toString())));

        final CitizenResource actualResponse = restClient.findById(fiscalCode);

        assertEquals(jsonNode, objectMapper.valueToTree(actualResponse));
    }

    @Test
    public void update() {
    }


}