package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import eu.sia.meda.connector.rest.transformer.response.SimpleRest2xxResponseTransformer;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import org.springframework.stereotype.Service;


@Service
class PaymentInstrumentUpdateResponseTransformer
        extends SimpleRest2xxResponseTransformer<PaymentInstrumentResource> {
}
