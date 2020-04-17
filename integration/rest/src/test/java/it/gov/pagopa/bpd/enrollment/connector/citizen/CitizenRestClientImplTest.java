package it.gov.pagopa.bpd.enrollment.connector.citizen;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.config.ArchConfiguration;
import eu.sia.meda.connector.rest.model.RestConnectorRequest;
import eu.sia.meda.connector.rest.model.RestConnectorResponse;
import eu.sia.meda.connector.rest.transformer.request.SimpleRestGetRequestTransformer;
import it.gov.pagopa.bpd.common.BaseTest;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.HashMap;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

public class CitizenRestClientImplTest extends BaseTest {

    private final CitizenRestClientImpl restClient;
    private final CitizenFindByIdRestConnector connector;
    private final SimpleRestGetRequestTransformer requestTransformer;
    private final CitizenFindByIdResponseTransformer responseTransformer;
    private final ObjectMapper mapper = new ArchConfiguration().objectMapper();

    private HttpStatus httpStatus;
    @Captor
    private ArgumentCaptor<HashMap<String, String>> requestArgsCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorRequest<Void>> connectorRequestCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorResponse<CitizenResource>> connectorResponseCaptor;


    public CitizenRestClientImplTest() {
        connector = Mockito.mock(CitizenFindByIdRestConnector.class);
        requestTransformer = Mockito.spy(new SimpleRestGetRequestTransformer());
        responseTransformer = Mockito.spy(new CitizenFindByIdResponseTransformer());
        restClient = new CitizenRestClientImpl(connector, requestTransformer, responseTransformer);

        configureTest();
    }


    private void configureTest() {
        when(connector.call(isNull(), any(SimpleRestGetRequestTransformer.class), any(CitizenFindByIdResponseTransformer.class), any(HashMap.class)))
                .thenAnswer(invocation -> {
                    RestConnectorRequest<Void> connectorRequest;
                    if (invocation.getArguments().length < 4) {
                        connectorRequest = requestTransformer.transform(invocation.getArgument(0));

                    } else {
                        //noinspection RedundantArrayCreation
                        connectorRequest = requestTransformer.transform(invocation.getArgument(0),
                                new Object[]{invocation.getArgument(3)});
                    }

                    String fiscalCode = connectorRequest.getParams().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY);
                    InputStream mockedJson = getClass().getClassLoader().getResourceAsStream(format("citizen/citizen_%s.json", fiscalCode));
                    CitizenResource resource = mapper.readValue(mockedJson, CitizenResource.class);
                    ResponseEntity<CitizenResource> responseEntity = new ResponseEntity<>(resource, httpStatus);
                    RestConnectorResponse<CitizenResource> restResponse = new RestConnectorResponse<>();
                    restResponse.setResponse(responseEntity);

                    return responseTransformer.transform(restResponse);
                });
    }


    @Test
    public void findById_OK() {
        httpStatus = HttpStatus.OK;
        final String fiscalCodeValue = "fiscalCode1";

        final CitizenResource result = restClient.findById(fiscalCodeValue);

        assertNotNull(result);
        assertEquals(fiscalCodeValue, result.getFiscalCode());

        checkInvocations(fiscalCodeValue);
    }


    private void checkInvocations(String fiscalCodeValue) {
        verify(connector, times(1))
                .call(isNull(), eq(requestTransformer), eq(responseTransformer), requestArgsCaptor.capture());
        assertNotNull(requestArgsCaptor.getValue());
        assertEquals(1, requestArgsCaptor.getValue().size());
        assertEquals(fiscalCodeValue, requestArgsCaptor.getValue().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY));

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
        assertEquals(httpStatus, connectorResponseCaptor.getValue().getResponse().getStatusCode());
        assertNotNull(connectorResponseCaptor.getValue().getResponse().getBody());
        assertEquals(fiscalCodeValue, connectorResponseCaptor.getValue().getResponse().getBody().getFiscalCode());

        verifyNoMoreInteractions(connector, requestTransformer, responseTransformer);
    }


    @Test(expected = IllegalStateException.class)
    public void findById_ResponseStatusNotValid() {
        httpStatus = HttpStatus.BAD_REQUEST;
        final String fiscalCodeValue = "fiscalCode1";

        try {
            restClient.findById(fiscalCodeValue);

        } finally {
            checkInvocations(fiscalCodeValue);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void findById_InvalidFiscalCode() {
        try {
            restClient.findById(null);

        } finally {
            Mockito.verifyZeroInteractions(connector, requestTransformer, responseTransformer);
        }
    }

}