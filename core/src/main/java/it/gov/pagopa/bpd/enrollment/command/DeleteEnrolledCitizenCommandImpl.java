package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.core.command.BaseCommand;
import feign.FeignException;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.ChannelValidationResource;
import it.gov.pagopa.bpd.enrollment.exception.PaymentInstrumenException;
import it.gov.pagopa.bpd.enrollment.exception.PaymentInstrumenWarnException;
import it.gov.pagopa.bpd.enrollment.exception.PaymentInstrumentDifferentChannelException;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeleteEnrolledCitizenCommandImpl extends BaseCommand<Boolean> implements DeleteEnrolledCitizenCommand {

    private final String fiscalCode;
    private final String channel;

    private CitizenService citizenService;
    private PaymentInstrumentService paymentInstrumentService;

    public DeleteEnrolledCitizenCommandImpl(String fiscalCode, String channel) {
        this.fiscalCode = fiscalCode;
        this.channel = channel;
    }

    @Override
    protected Boolean doExecute() throws ExecutionException, InterruptedException {

        ChannelValidationResource channelValidationResource = null;

        try {
            try {
                channelValidationResource = paymentInstrumentService.validateChannel(fiscalCode, channel);
                if (channelValidationResource == null || !channelValidationResource.getIsValid()) {
                    throw new Exception();
                }
            } catch (FeignException e) {
                if (e.contentUTF8().startsWith("{\"returnMessages\":") && e.status() >= 400 && e.status() < 500) {
                    PaymentInstrumenWarnException paymentInstrumenWarnException = new PaymentInstrumenWarnException(e.getMessage());
                    paymentInstrumenWarnException.initCause(e);
                    throw paymentInstrumenWarnException;
                }
                PaymentInstrumenException paymentInstrumenException = new PaymentInstrumenException(e.getMessage());
                paymentInstrumenException.initCause(e);
                throw paymentInstrumenException;
            } catch (Exception e) {
                PaymentInstrumenException paymentInstrumenException = new PaymentInstrumenException(e.getMessage());
                paymentInstrumenException.initCause(e);
                throw paymentInstrumenException;
            }

        } catch (Exception e) {
            if (logger.isWarnEnabled() && e.getCause() instanceof PaymentInstrumenWarnException) {
                logger.warn(e.getMessage(), e);
            } else if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }

            if (e.getCause() instanceof PaymentInstrumenWarnException
                    && ((FeignException) e.getCause().getCause()).status() == 400) {
                throw new PaymentInstrumentDifferentChannelException(e.getMessage());
            }
            throw new RuntimeException("Uncapable to complete citizen deletion");
        }
        try {
            citizenService.delete(fiscalCode);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
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
