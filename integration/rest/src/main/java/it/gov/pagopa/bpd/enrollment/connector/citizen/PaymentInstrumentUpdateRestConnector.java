package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.meda.MedaInternalConnector;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.springframework.stereotype.Service;


@Service
class PaymentInstrumentUpdateRestConnector
        extends MedaInternalConnector<PaymentInstrumentDTO, PaymentInstrumentResource, PaymentInstrumentDTO, PaymentInstrumentResource> {
}
