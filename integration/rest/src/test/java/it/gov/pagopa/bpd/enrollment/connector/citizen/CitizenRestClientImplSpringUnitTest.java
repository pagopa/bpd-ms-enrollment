package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.rest.BaseRestConnectorTest;
import eu.sia.meda.connector.rest.model.RestConnectorRequest;
import eu.sia.meda.connector.rest.model.RestConnectorResponse;
import eu.sia.meda.connector.rest.transformer.request.SimpleRestGetRequestTransformer;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;


@TestPropertySource(
        locations = {
                "classpath:config/citizen/CitizenFindByIdRestConnector.properties"
        },
        properties = {
                "connectors.medaInternalConfigurations.items.CitizenFindByIdRestConnector.mocked=true",
                "connectors.medaInternalConfigurations.items.CitizenFindByIdRestConnector.path=citizen/findById"
        })
@Import({CitizenRestClientImpl.class})
public class CitizenRestClientImplSpringUnitTest extends BaseRestConnectorTest {

    @Autowired
    private CitizenRestClientImpl restClient;

    @SpyBean
    private CitizenFindByIdRestConnector connector;
    @SpyBean
    private SimpleRestGetRequestTransformer requestTransformer;
    @SpyBean
    private CitizenFindByIdResponseTransformer responseTransformer;

    @Captor
    private ArgumentCaptor<HashMap<String, String>> requestArgsCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorRequest<Void>> connectorRequestCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorResponse<CitizenResource>> connectorResponseCaptor;

    @Test
    public void findById_OK() {
        final String fiscalCode = "prova";
        final CitizenResource result = restClient.findById(fiscalCode);

        assertNotNull(result);
        assertEquals(fiscalCode, result.getFiscalCode());
        assertNotNull(result.getTimestampTC());

        checkInvocations(fiscalCode);
    }

    private void checkInvocations(String fiscalCode) {
        verify(connector, times(1))
                .call(isNull(), eq(requestTransformer), eq(responseTransformer), requestArgsCaptor.capture());
        assertNotNull(requestArgsCaptor.getValue());
        assertEquals(1, requestArgsCaptor.getValue().size());
        assertEquals(fiscalCode, requestArgsCaptor.getValue().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY));

        verify(requestTransformer, times(1))
                .transform(isNull(), eq(requestArgsCaptor.getValue()));
        verify(requestTransformer, times(1))
                .readArgs(connectorRequestCaptor.capture(), eq(requestArgsCaptor.getValue()));
        assertNotNull(connectorRequestCaptor.getValue());
        assertNotNull(connectorRequestCaptor.getValue().getMethod());
        assertEquals(HttpMethod.GET, connectorRequestCaptor.getValue().getMethod());

        verify(responseTransformer, times(1))
                .transform(connectorResponseCaptor.capture());
        assertNotNull(connectorResponseCaptor.getValue());
        assertNotNull(connectorResponseCaptor.getValue().getResponse());
        assertEquals(HttpStatus.OK, connectorResponseCaptor.getValue().getResponse().getStatusCode());
        assertNotNull(connectorResponseCaptor.getValue().getResponse().getBody());
        assertEquals(fiscalCode, connectorResponseCaptor.getValue().getResponse().getBody().getFiscalCode());

        verifyNoMoreInteractions(connector, requestTransformer, responseTransformer);
    }

    //    @Test
    public void findById_ResponseStatusNotValid() {
        final String fiscalCode = "prova";
        final CitizenResource result = restClient.findById(fiscalCode);

        assertNotNull(result);
        assertEquals(fiscalCode, result.getFiscalCode());
        assertNotNull(result.getTimestampTC());

        checkInvocations(fiscalCode);
    }

}