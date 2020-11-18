package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;

import java.time.OffsetDateTime;

/**
 * Service to manage the Business Logic related to Payment Instrument
 */
public interface PaymentInstrumentService {

    PaymentInstrumentResource update(String hpan, PaymentInstrumentDto paymentInstrument);

    void deleteByFiscalCode(String fiscalCode, String channel);

    void rollback(String fiscalCode, OffsetDateTime requestTimestamp);

}
