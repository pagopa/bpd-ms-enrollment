package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import eu.sia.meda.connector.rest.model.RestConnectorRequest;
import eu.sia.meda.connector.rest.model.RestConnectorResponse;
import it.gov.pagopa.bpd.common.BaseTest;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PaymentInstrumentRestClientImplTest extends BaseTest {

    private final PaymentInstrumentRestClientImpl restClient;
    private final PaymentInstrumentUpdateRestConnector connector;
    private final PaymentInstrumentUpdateRequestTransformer requestTransformer;
    private final PaymentInstrumentUpdateResponseTransformer responseTransformer;

    private HttpStatus httpStatus;
    @Captor
    private ArgumentCaptor<HashMap<String, String>> requestArgsCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorRequest<PaymentInstrumentDto>> connectorRequestCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorResponse<PaymentInstrumentResource>> connectorResponseCaptor;


    public PaymentInstrumentRestClientImplTest() {
        connector = Mockito.mock(PaymentInstrumentUpdateRestConnector.class);
        requestTransformer = Mockito.spy(new PaymentInstrumentUpdateRequestTransformer());
        responseTransformer = Mockito.spy(new PaymentInstrumentUpdateResponseTransformer());
        restClient = new PaymentInstrumentRestClientImpl(connector, requestTransformer, responseTransformer);

        configureTest();
    }


    private void configureTest() {
        when(connector.call(any(PaymentInstrumentDto.class), any(PaymentInstrumentUpdateRequestTransformer.class), any(PaymentInstrumentUpdateResponseTransformer.class), any(HashMap.class)))
                .thenAnswer(invocation -> {
                    final PaymentInstrumentDto input = invocation.getArgument(0, PaymentInstrumentDto.class);
                    final RestConnectorRequest<PaymentInstrumentDto> connectorRequest;
                    if (invocation.getArguments().length < 4) {
                        connectorRequest = requestTransformer.transform(input);

                    } else {
                        //noinspection RedundantArrayCreation
                        connectorRequest = requestTransformer.transform(input,
                                new Object[]{invocation.getArgument(3)});
                    }

                    PaymentInstrumentResource resource = new PaymentInstrumentResource();
                    resource.setHpan(connectorRequest.getParams().get(PaymentInstrumentRestClientImpl.HASH_PAN_PARAM_KEY));
                    resource.setFiscalCode(input.getFiscalCode());
                    resource.setActivationDate(input.getActivationDate());
                    ResponseEntity<PaymentInstrumentResource> responseEntity = new ResponseEntity<>(resource, httpStatus);
                    RestConnectorResponse<PaymentInstrumentResource> restResponse = new RestConnectorResponse<>();
                    restResponse.setResponse(responseEntity);

                    return responseTransformer.transform(restResponse);
                });
    }


    @Test
    public void update_OK() {
        httpStatus = HttpStatus.OK;

        String hashPan = RandomStringUtils.randomNumeric(16);
        PaymentInstrumentDto input = new PaymentInstrumentDto();
        input.setFiscalCode(RandomStringUtils.randomAlphanumeric(16));
        input.setActivationDate(OffsetDateTime.now());

        final PaymentInstrumentResource result = restClient.update(hashPan, input);

        assertNotNull(result);
        assertEquals(hashPan, result.getHpan());
        assertEquals(input.getFiscalCode(), result.getFiscalCode());
        assertEquals(input.getActivationDate(), result.getActivationDate());

        checkInvocations(hashPan, input);
    }

    private void checkInvocations(String hashPan, PaymentInstrumentDto input) {
        verify(connector, times(1))
                .call(eq(input), eq(requestTransformer), eq(responseTransformer), requestArgsCaptor.capture());
        assertNotNull(requestArgsCaptor.getValue());
        assertEquals(1, requestArgsCaptor.getValue().size());
        assertEquals(hashPan, requestArgsCaptor.getValue().get(PaymentInstrumentRestClientImpl.HASH_PAN_PARAM_KEY));

        verify(requestTransformer, times(1))
                .transform(eq(input), eq(requestArgsCaptor.getValue()));
        verify(requestTransformer, times(1))
                .readArgs(connectorRequestCaptor.capture(), eq(requestArgsCaptor.getValue()));
        assertNotNull(connectorRequestCaptor.getValue());
        assertNotNull(connectorRequestCaptor.getValue().getMethod());
        assertEquals(HttpMethod.PUT, connectorRequestCaptor.getValue().getMethod());

        verify(responseTransformer, times(1))
                .transform(connectorResponseCaptor.capture());
        assertNotNull(connectorResponseCaptor.getValue());
        assertNotNull(connectorResponseCaptor.getValue().getResponse());
        assertEquals(httpStatus, connectorResponseCaptor.getValue().getResponse().getStatusCode());
        assertNotNull(connectorResponseCaptor.getValue().getResponse().getBody());
        assertEquals(hashPan, connectorResponseCaptor.getValue().getResponse().getBody().getHpan());
        assertEquals(input.getFiscalCode(), connectorResponseCaptor.getValue().getResponse().getBody().getFiscalCode());
        assertEquals(input.getActivationDate(), connectorResponseCaptor.getValue().getResponse().getBody().getActivationDate());

        verifyNoMoreInteractions(connector, requestTransformer, responseTransformer);
    }

    @Test(expected = IllegalStateException.class)
    public void update_ResponseStatusNotValid() {
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        String hashPan = RandomStringUtils.randomNumeric(16);
        PaymentInstrumentDto input = new PaymentInstrumentDto();
        input.setFiscalCode(RandomStringUtils.randomAlphanumeric(16));
        input.setActivationDate(OffsetDateTime.now());

        try {
            restClient.update(hashPan, input);

        } finally {
            checkInvocations(hashPan, input);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void findById_InvalidHashPan() {
        try {
            restClient.update(null, new PaymentInstrumentDto());

        } finally {
            Mockito.verifyZeroInteractions(connector, requestTransformer, responseTransformer);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void findById_InvalidDto() {
        try {
            restClient.update("", null);

        } finally {
            Mockito.verifyZeroInteractions(connector, requestTransformer, responseTransformer);
        }
    }

}