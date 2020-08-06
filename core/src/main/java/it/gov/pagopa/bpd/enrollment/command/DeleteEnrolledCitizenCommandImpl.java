package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeleteEnrolledCitizenCommandImpl extends BaseCommand<Boolean> implements DeleteEnrolledCitizenCommand {

    private final String fiscalCode;

    private CitizenService citizenService;
    private PaymentInstrumentService paymentInstrumentService;

    public DeleteEnrolledCitizenCommandImpl(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    @Override
    protected Boolean doExecute() {
        final PaymentInstrumentResource paymentInstrumentResource;

        try {
            citizenService.delete(fiscalCode);
            paymentInstrumentService.deleteByFiscalCode(fiscalCode);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            return false;
        }

        return true;
    }

    @Autowired
    public void setCitizenService(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @Autowired
    public void setPaymentInstrumentService(PaymentInstrumentService paymentInstrumentService) {
        this.paymentInstrumentService = paymentInstrumentService;
    }

}
