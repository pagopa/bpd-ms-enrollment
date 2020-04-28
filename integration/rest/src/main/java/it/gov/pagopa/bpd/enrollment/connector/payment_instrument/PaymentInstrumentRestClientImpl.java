package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @see it.gov.pagopa.bpd.enrollment.connector.payment_instrument.PaymentInstrumentRestClient
 */
@Service
class PaymentInstrumentRestClientImpl implements PaymentInstrumentRestClient {

    static final String HASH_PAN_PARAM_KEY = "hashPan";

    private final PaymentInstrumentUpdateRestConnector restConnector;
    private final PaymentInstrumentUpdateRequestTransformer requestTransformer;
    private final PaymentInstrumentUpdateResponseTransformer responseTransformer;


    @Autowired
    public PaymentInstrumentRestClientImpl(PaymentInstrumentUpdateRestConnector restConnector,
                                           PaymentInstrumentUpdateRequestTransformer requestTransformer,
                                           PaymentInstrumentUpdateResponseTransformer responseTransformer) {
        this.restConnector = restConnector;
        this.requestTransformer = requestTransformer;
        this.responseTransformer = responseTransformer;
    }


    @Override
    public PaymentInstrumentResource update(String hashPan, PaymentInstrumentDto request) {
        if (hashPan == null) {
            throw new IllegalArgumentException("Hashed PAN cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request body cannot be null");
        }

        final HashMap<String, Object> params = new HashMap<>();
        params.put(HASH_PAN_PARAM_KEY, hashPan);

        return restConnector.call(request, requestTransformer, responseTransformer, params);
    }

}
