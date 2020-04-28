package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;

/**
 * Payment Instrument Rest Client
 */
public interface PaymentInstrumentRestClient {

    PaymentInstrumentResource update(String hashPan, PaymentInstrumentDto paymentInstrumentDTO);

}
