package it.gov.pagopa.bpd.enrollment.connector.citizen;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;

public interface PaymentInstrumentRestClient {

    PaymentInstrumentResource update(String hashPan, PaymentInstrumentDto paymentInstrumentDTO);

}
