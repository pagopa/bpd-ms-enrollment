package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;

public interface PaymentInstrumentService {

    PaymentInstrumentResource update(String hpan, PaymentInstrumentDTO paymentInstrument);

}
