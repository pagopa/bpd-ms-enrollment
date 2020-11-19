package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.core.command.BaseCommand;
import feign.FeignException;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import it.gov.pagopa.bpd.enrollment.service.WinningTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeleteEnrolledCitizenCommandImpl extends BaseCommand<Boolean> implements DeleteEnrolledCitizenCommand {

    private final String fiscalCode;
    private final String channel;

    private CitizenService citizenService;
    private PaymentInstrumentService paymentInstrumentService;
    private WinningTransactionService winningTransactionService;
    private OffsetDateTime requestTimestamp = OffsetDateTime.now();

    public DeleteEnrolledCitizenCommandImpl(String fiscalCode, String channel) {
        this.fiscalCode = fiscalCode;
        this.channel = channel;
    }

    @Override
    protected Boolean doExecute() {
        final PaymentInstrumentResource paymentInstrumentResource;

        try {
            paymentInstrumentService.deleteByFiscalCode(fiscalCode, channel);
            winningTransactionService.deleteByFiscalCode(fiscalCode);
        } catch (FeignException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            paymentInstrumentService.rollback(fiscalCode, requestTimestamp);
            winningTransactionService.rollback(fiscalCode, requestTimestamp);
            if (e.status() == 400) {
                throw e;
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            paymentInstrumentService.rollback(fiscalCode, requestTimestamp);
            winningTransactionService.rollback(fiscalCode, requestTimestamp);
            return false;
        }
        try {
            citizenService.delete(fiscalCode);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            paymentInstrumentService.rollback(fiscalCode, requestTimestamp);
            winningTransactionService.rollback(fiscalCode, requestTimestamp);
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

    @Autowired
    public void setWinningTransactionService(WinningTransactionService winningTransactionService) {
        this.winningTransactionService = winningTransactionService;
    }


}
