package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import eu.sia.meda.connector.meda.MedaInternalConnector;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import org.springframework.stereotype.Service;


@Service
class PaymentInstrumentUpdateRestConnector
        extends MedaInternalConnector<PaymentInstrumentDto, PaymentInstrumentResource, PaymentInstrumentDto, PaymentInstrumentResource> {
}
