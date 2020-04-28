package it.gov.pagopa.bpd.enrollment.factory;

import it.gov.pagopa.bpd.common.factory.ModelFactory;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @see it.gov.pagopa.bpd.common.factory.ModelFactory
 */
@Component
public class PaymentInstrumentFactory implements ModelFactory<EnrollmentPaymentInstrumentDto, PaymentInstrumentDto> {

    @Override
    public PaymentInstrumentDto apply(EnrollmentPaymentInstrumentDto dto) {
        final PaymentInstrumentDto result = new PaymentInstrumentDto();

        BeanUtils.copyProperties(dto, result);

        return result;
    }

}
