package it.gov.pagopa.bpd.enrollment.assembler;

import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Mapper between <Citizen> Entity class and <CitizenResource> Resource class
 */
@Service
public class PaymentInstrumentResourceAssembler {

    public EnrollmentPaymentInstrumentResource toResource(PaymentInstrumentResource paymentInstrumentResource) {
        EnrollmentPaymentInstrumentResource resource = null;

        if (paymentInstrumentResource != null) {
            resource = new EnrollmentPaymentInstrumentResource();
            BeanUtils.copyProperties(paymentInstrumentResource, resource);
        }

        return resource;
    }
}
