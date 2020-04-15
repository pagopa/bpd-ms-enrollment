package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.rest.transformer.response.SimpleRest2xxResponseTransformer;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
class PaymentInstrumentRestClientImpl implements PaymentInstrumentRestClient {

    private final PaymentInstrumentUpdateRestConnector restConnector;
    private final PaymentInstrumentUpdateRequestTransformer requestTransformer;
    private final SimpleRest2xxResponseTransformer<PaymentInstrumentResource> responseTransformer;


    @Autowired
    public PaymentInstrumentRestClientImpl(PaymentInstrumentUpdateRestConnector restConnector,
                                           PaymentInstrumentUpdateRequestTransformer requestTransformer,
                                           SimpleRest2xxResponseTransformer<PaymentInstrumentResource> responseTransformer) {
        this.restConnector = restConnector;
        this.requestTransformer = requestTransformer;
        this.responseTransformer = responseTransformer;
    }


    @Override
    public PaymentInstrumentResource update(String hashPan, PaymentInstrumentDto paymentInstrumentDTO) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("hashPan", hashPan);

        return restConnector.call(paymentInstrumentDTO, requestTransformer, responseTransformer, params);
    }

}
