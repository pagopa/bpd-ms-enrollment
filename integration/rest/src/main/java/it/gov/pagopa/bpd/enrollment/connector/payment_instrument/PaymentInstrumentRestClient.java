package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;

public interface PaymentInstrumentRestClient {

    PaymentInstrumentResource update(String hashPan, PaymentInstrumentDto paymentInstrumentDTO);

}
