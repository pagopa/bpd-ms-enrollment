package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.rest.model.RestConnectorRequest;
import eu.sia.meda.connector.rest.model.RestConnectorResponse;
import eu.sia.meda.connector.rest.transformer.request.SimpleRestGetRequestTransformer;
import eu.sia.meda.connector.rest.transformer.response.SimpleRest2xxResponseTransformer;
import it.gov.pagopa.bpd.common.BaseTest;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

public class CitizenRestClientImplTest extends BaseTest {

    private final CitizenRestClientImpl restClient;
    private final CitizenFindByIdRestConnector connector;
    private final SimpleRestGetRequestTransformer requestTransformer;
    private final SimpleRest2xxResponseTransformer<CitizenResource> responseTransformer;

    private HttpStatus httpStatus;
    @Captor
    private ArgumentCaptor<HashMap<String, String>> requestArgsCaptor;
    @Captor
    private ArgumentCaptor<RestConnectorResponse<CitizenResource>> connectorResponseCaptor;


    public CitizenRestClientImplTest() {
        connector = Mockito.mock(CitizenFindByIdRestConnector.class);
        requestTransformer = Mockito.spy(new SimpleRestGetRequestTransformer());
        responseTransformer = Mockito.spy(new SimpleRest2xxResponseTransformer<>());
        restClient = new CitizenRestClientImpl(connector, requestTransformer, responseTransformer);

        configureTest();
    }


    private void configureTest() {
        //noinspection unchecked
        when(connector.call(isNull(), any(SimpleRestGetRequestTransformer.class), any(SimpleRest2xxResponseTransformer.class), any(HashMap.class)))
                .thenAnswer(invocation -> {
                    final RestConnectorRequest<Void> connectorRequest;
                    if (invocation.getArguments().length < 4) {
                        connectorRequest = requestTransformer.transform(invocation.getArgument(0));

                    } else {
                        //noinspection RedundantArrayCreation
                        connectorRequest = requestTransformer.transform(invocation.getArgument(0),
                                new Object[]{invocation.getArgument(3)});
                    }

                    CitizenResource resource = new CitizenResource();
                    resource.setFiscalCode(connectorRequest.getParams().get("fiscalCode"));
                    ResponseEntity<CitizenResource> responseEntity = new ResponseEntity<>(resource, httpStatus);
                    RestConnectorResponse<CitizenResource> restResponse = new RestConnectorResponse<>();
                    restResponse.setResponse(responseEntity);

                    return responseTransformer.transform(restResponse);
                });
    }


    @Test
    public void findById_OK() {
        httpStatus = HttpStatus.OK;
        final String fiscalCodeValue = RandomStringUtils.randomAlphanumeric(16);

        final CitizenResource result = restClient.findById(fiscalCodeValue);

        Assert.assertNotNull(result);
        Assert.assertEquals(fiscalCodeValue, result.getFiscalCode());

        BDDMockito.verify(connector, times(1))
                .call(isNull(), eq(requestTransformer), eq(responseTransformer), requestArgsCaptor.capture());
        Assert.assertNotNull(requestArgsCaptor.getValue());
        Assert.assertEquals(1, requestArgsCaptor.getValue().size());
        Assert.assertEquals(fiscalCodeValue, requestArgsCaptor.getValue().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY));

        BDDMockito.verify(requestTransformer, times(1)).transform(isNull(), eq(requestArgsCaptor.getValue()));

        BDDMockito.verify(responseTransformer, times(1)).transform(connectorResponseCaptor.capture());
        Assert.assertNotNull(connectorResponseCaptor.getValue());
        final ResponseEntity<CitizenResource> actualResponse = connectorResponseCaptor.getValue().getResponse();
        Assert.assertNotNull(actualResponse);
        Assert.assertEquals(httpStatus, actualResponse.getStatusCode());
        Assert.assertNotNull(actualResponse.getBody());
        Assert.assertEquals(fiscalCodeValue, actualResponse.getBody().getFiscalCode());

    }


    @Test(expected = IllegalStateException.class)
    public void findById_ResponseStatusNotValid() {
        httpStatus = HttpStatus.BAD_REQUEST;
        final String fiscalCodeValue = RandomStringUtils.randomAlphanumeric(16);

        try {
            restClient.findById(fiscalCodeValue);

        } finally {
            BDDMockito.verify(connector, times(1))
                    .call(isNull(), eq(requestTransformer), eq(responseTransformer), requestArgsCaptor.capture());
            Assert.assertNotNull(requestArgsCaptor.getValue());
            Assert.assertEquals(1, requestArgsCaptor.getValue().size());
            Assert.assertEquals(fiscalCodeValue, requestArgsCaptor.getValue().get(CitizenRestClientImpl.FISCAL_CODE_PARAM_KEY));

            BDDMockito.verify(requestTransformer, times(1)).transform(isNull(), eq(requestArgsCaptor.getValue()));

            BDDMockito.verify(responseTransformer, times(1)).transform(connectorResponseCaptor.capture());
            Assert.assertNotNull(connectorResponseCaptor.getValue());
            final ResponseEntity<CitizenResource> actualResponse = connectorResponseCaptor.getValue().getResponse();
            Assert.assertNotNull(actualResponse);
            Assert.assertEquals(httpStatus, actualResponse.getStatusCode());
            Assert.assertNotNull(actualResponse.getBody());
            Assert.assertEquals(fiscalCodeValue, actualResponse.getBody().getFiscalCode());
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void findById_IllegalArgument() {
        try {
            restClient.findById(null);

        } finally {
            Mockito.verifyZeroInteractions(connector, requestTransformer, responseTransformer);
        }
    }

}