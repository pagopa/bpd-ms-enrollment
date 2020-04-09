package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class EnrollCitizenCommandImpl extends BaseCommand<PaymentInstrumentResource> implements EnrollPaymentInstrumentCommand {

    private final String hashPan;
    private final PaymentInstrumentDTO paymentInstrumentDTO;
    @Autowired
    private CitizenService citizenService;
    @Autowired
    private PaymentInstrumentService paymentInstrumentService;


    public EnrollCitizenCommandImpl(String hashPan, PaymentInstrumentDTO paymentInstrumentDTO) {
        this.hashPan = hashPan;
        this.paymentInstrumentDTO = paymentInstrumentDTO;
    }


    @Override
    protected PaymentInstrumentResource doExecute() {
        final PaymentInstrumentResource paymentInstrumentResource;

        final CitizenResource citizen = citizenService.findById("test");
        if (citizen.isEnabled()) {
            paymentInstrumentResource = paymentInstrumentService.update(hashPan, paymentInstrumentDTO);

        } else {
            throw new RuntimeException("Citizen not enabled");
        }

        return paymentInstrumentResource;
    }

}
