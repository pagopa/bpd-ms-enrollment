package it.gov.pagopa.bpd.enrollment.api.factory;

import it.gov.pagopa.bpd.enrollment.api.model.EnrollmentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import org.springframework.stereotype.Component;

@Component
class PaymentInstrumentFactory implements ModelFactory<EnrollmentDTO, PaymentInstrumentDTO> {

    @Override
    public PaymentInstrumentDTO createModel(EnrollmentDTO dto) {
        final PaymentInstrumentDTO result = new PaymentInstrumentDTO();

        result.setActivationDate(dto.getActivationDate());

        return result;
    }

}
