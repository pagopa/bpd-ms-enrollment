package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.meda.MedaInternalConnector;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.springframework.stereotype.Service;


@Service
class PaymentInstrumentUpdateRestConnector
        extends MedaInternalConnector<PaymentInstrumentDto, PaymentInstrumentResource, PaymentInstrumentDto, PaymentInstrumentResource> {
}
