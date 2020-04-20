package it.gov.pagopa.bpd.enrollment.connector.citizen;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.config.ArchConfiguration;
import eu.sia.meda.connector.rest.model.RestConnectorRequest;
import eu.sia.meda.connector.rest.model.RestConnectorResponse;
import eu.sia.meda.connector.rest.transformer.request.SimpleRestGetRequestTransformer;
import it.gov.pagopa.bpd.common.BaseTest;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.HashMap;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

public class CitizenRestClientImplTest extends BaseTest {

    private final CitizenRestClientImpl restClient;
    private final CitizenFindByIdRestConnector findByIdRestConnector;
    private final SimpleRestGetRequestTransformer getRequestTransformer;
    private final CitizenFindByIdResponseTransformer findByIdResponseTransformer;
    private final ObjectMapper mapper = new ArchConfiguration().objectMapper();
    private final CitizenUpdateRestConnector updateRestConnector;
    private final CitizenUpdateRequestTransformer updateRequestTransformer;
    private final CitizenUpdateResponseTransformer updateResponseTransformer;

    private HttpStatus httpStatus;
    @Captor
    private ArgumentCaptor<HashMap<String, String>> requestArgsCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorRequest<Void>> findByIdConnectorRequestCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorResponse<CitizenResource>> connectorResponseCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorRequest<CitizenDto>> updateConnectorRequestCaptor;


    public CitizenRestClientImplTest() {
        findByIdRestConnector = Mockito.mock(CitizenFindByIdRestConnector.class);
        getRequestTransformer = Mockito.spy(new SimpleRestGetRequestTransformer());
        findByIdResponseTransformer = Mockito.spy(new CitizenFindByIdResponseTransformer());
        updateRestConnector = Mockito.mock(CitizenUpdateRestConnector.class);
        updateRequestTransformer = Mockito.spy(new CitizenUpdateRequestTransformer());
        updateResponseTransformer = Mockito.spy(new CitizenUpdateResponseTransformer());
        restClient = new CitizenRestClientImpl(findByIdRestConnector,
                getRequestTransformer,
                findByIdResponseTransformer,
                updateRestConnector,
                updateRequestTransformer,
                updateResponseTransformer);

        configureFindByIdTests();
        configureUpdateTests();
    }


    private void configureFindByIdTests() {
        when(findByIdRestConnector.call(isNull(), any(SimpleRestGetRequestTransformer.class), any(CitizenFindByIdResponseTransformer.class), anyMap()))
                .thenAnswer(invocation -> {
                    RestConnectorRequest<Void> connectorRequest;
                    if (invocation.getArguments().length < 4) {
                        connectorRequest = getRequestTransformer.transform(invocation.getArgument(0));

                    } else {
                        //noinspection RedundantArrayCreation
                        connectorRequest = getRequestTransformer.transform(invocation.getArgument(0),
                                new Object[]{invocation.getArgument(3)});
                    }

                    String fiscalCode = connectorRequest.getParams().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY);
                    InputStream mockedJson = getClass()
                            .getClassLoader()
                            .getResourceAsStream(format("citizen/citizen_%s.json", fiscalCode));
                    CitizenResource resource = mapper.readValue(mockedJson, CitizenResource.class);
                    ResponseEntity<CitizenResource> responseEntity = new ResponseEntity<>(resource, httpStatus);
                    RestConnectorResponse<CitizenResource> restResponse = new RestConnectorResponse<>();
                    restResponse.setResponse(responseEntity);

                    return findByIdResponseTransformer.transform(restResponse);
                });
    }


    private void configureUpdateTests() {
        when(updateRestConnector.call(any(CitizenDto.class), any(CitizenUpdateRequestTransformer.class), any(CitizenUpdateResponseTransformer.class), anyMap()))
                .thenAnswer(invocation -> {
                    RestConnectorRequest<CitizenDto> connectorRequest;
                    if (invocation.getArguments().length < 4) {
                        connectorRequest = updateRequestTransformer.transform(invocation.getArgument(0));

                    } else {
                        //noinspection RedundantArrayCreation
                        connectorRequest = updateRequestTransformer.transform(invocation.getArgument(0),
                                new Object[]{invocation.getArgument(3)});
                    }

                    String fiscalCode = connectorRequest.getParams().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY);
                    InputStream mockedJson = getClass()
                            .getClassLoader()
                            .getResourceAsStream(format("citizen/citizen_%s.json", fiscalCode));
                    CitizenResource resource = mapper.readValue(mockedJson, CitizenResource.class);
                    ResponseEntity<CitizenResource> responseEntity = new ResponseEntity<>(resource, httpStatus);
                    RestConnectorResponse<CitizenResource> restResponse = new RestConnectorResponse<>();
                    restResponse.setResponse(responseEntity);

                    return updateResponseTransformer.transform(restResponse);
                });
    }


    @Test
    public void findById_OK() {
        httpStatus = HttpStatus.OK;
        final String fiscalCodeValue = "fiscalCode1";

        final CitizenResource result = restClient.findById(fiscalCodeValue);

        assertNotNull(result);
        assertEquals(fiscalCodeValue, result.getFiscalCode());

        checkFindByIdInvocations(fiscalCodeValue);
    }


    private void checkFindByIdInvocations(String fiscalCodeValue) {
        verify(findByIdRestConnector, times(1))
                .call(isNull(), eq(getRequestTransformer), eq(findByIdResponseTransformer), requestArgsCaptor.capture());
        assertNotNull(requestArgsCaptor.getValue());
        assertEquals(1, requestArgsCaptor.getValue().size());
        assertEquals(fiscalCodeValue, requestArgsCaptor.getValue().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY));

        verify(getRequestTransformer, times(1))
                .transform(isNull(), eq(requestArgsCaptor.getValue()));
        verify(getRequestTransformer, times(1))
                .readArgs(findByIdConnectorRequestCaptor.capture(), eq(requestArgsCaptor.getValue()));
        assertNotNull(findByIdConnectorRequestCaptor.getValue());
        assertNotNull(findByIdConnectorRequestCaptor.getValue().getMethod());
        assertEquals(HttpMethod.GET, findByIdConnectorRequestCaptor.getValue().getMethod());

        verify(findByIdResponseTransformer, times(1))
                .transform(connectorResponseCaptor.capture());
        assertNotNull(connectorResponseCaptor.getValue());
        assertNotNull(connectorResponseCaptor.getValue().getResponse());
        assertEquals(httpStatus, connectorResponseCaptor.getValue().getResponse().getStatusCode());
        assertNotNull(connectorResponseCaptor.getValue().getResponse().getBody());
        assertEquals(fiscalCodeValue, connectorResponseCaptor.getValue().getResponse().getBody().getFiscalCode());

        verifyNoMoreInteractions(findByIdRestConnector, getRequestTransformer, findByIdResponseTransformer);
    }


    @Test(expected = IllegalStateException.class)
    public void findById_ResponseStatusNotValid() {
        httpStatus = HttpStatus.BAD_REQUEST;
        final String fiscalCodeValue = "fiscalCode1";

        try {
            restClient.findById(fiscalCodeValue);

        } finally {
            checkFindByIdInvocations(fiscalCodeValue);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void findById_InvalidFiscalCode() {
        try {
            restClient.findById(null);

        } finally {
            Mockito.verifyZeroInteractions(findByIdRestConnector, getRequestTransformer, findByIdResponseTransformer);
        }
    }


    @Test
    public void update_OK() {
        httpStatus = HttpStatus.OK;

        final String fiscalCode = "fiscalCode1";
        final CitizenDto request = new CitizenDto();
        request.setTimestampTC(OffsetDateTime.now());

        final CitizenResource result = restClient.update(fiscalCode, request);

        assertNotNull(result);
        assertEquals(fiscalCode, result.getFiscalCode());
        assertNotNull(result.getTimestampTC());

        checkUpdateInvocations(fiscalCode, request);
    }


    private void checkUpdateInvocations(String fiscalCodeValue, CitizenDto request) {
        verify(updateRestConnector, times(1))
                .call(eq(request), eq(updateRequestTransformer), eq(updateResponseTransformer), requestArgsCaptor.capture());
        assertNotNull(requestArgsCaptor.getValue());
        assertEquals(1, requestArgsCaptor.getValue().size());
        assertEquals(fiscalCodeValue, requestArgsCaptor.getValue().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY));

        verify(updateRequestTransformer, times(1))
                .transform(eq(request), eq(requestArgsCaptor.getValue()));
        verify(updateRequestTransformer, times(1))
                .readArgs(updateConnectorRequestCaptor.capture(), eq(requestArgsCaptor.getValue()));
        assertNotNull(updateConnectorRequestCaptor.getValue());
        assertNotNull(updateConnectorRequestCaptor.getValue().getMethod());
        assertEquals(HttpMethod.PUT, updateConnectorRequestCaptor.getValue().getMethod());

        verify(updateResponseTransformer, times(1))
                .transform(connectorResponseCaptor.capture());
        assertNotNull(connectorResponseCaptor.getValue());
        assertNotNull(connectorResponseCaptor.getValue().getResponse());
        assertEquals(httpStatus, connectorResponseCaptor.getValue().getResponse().getStatusCode());
        assertNotNull(connectorResponseCaptor.getValue().getResponse().getBody());
        assertEquals(fiscalCodeValue, connectorResponseCaptor.getValue().getResponse().getBody().getFiscalCode());
        assertNotNull(connectorResponseCaptor.getValue().getResponse().getBody().getTimestampTC());

        verifyNoMoreInteractions(updateRestConnector, updateRequestTransformer, updateResponseTransformer);
    }


    @Test(expected = IllegalStateException.class)
    public void update_ResponseStatusNotValid() {
        httpStatus = HttpStatus.BAD_REQUEST;

        String fiscalCode = "fiscalCode1";
        CitizenDto request = new CitizenDto();
        request.setTimestampTC(OffsetDateTime.now());

        try {
            restClient.update(fiscalCode, request);

        } finally {
            checkUpdateInvocations(fiscalCode, request);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void update_InvalidFiscalCode() {
        try {
            restClient.update(null, new CitizenDto());

        } finally {
            Mockito.verifyZeroInteractions(updateRestConnector, updateRequestTransformer, updateResponseTransformer);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void update_InvalidRequest() {
        try {
            restClient.update("", null);

        } finally {
            Mockito.verifyZeroInteractions(updateRestConnector, updateRequestTransformer, updateResponseTransformer);
        }
    }

}