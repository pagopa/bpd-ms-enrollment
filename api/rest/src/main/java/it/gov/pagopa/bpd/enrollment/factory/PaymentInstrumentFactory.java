package it.gov.pagopa.bpd.enrollment.factory;

import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PaymentInstrumentFactory implements ModelFactory<EnrollmentDto, PaymentInstrumentDto> {

    @Override
    public PaymentInstrumentDto createModel(EnrollmentDto dto) {
        final PaymentInstrumentDto result = new PaymentInstrumentDto();

        BeanUtils.copyProperties(dto, result);

        return result;
    }

}
