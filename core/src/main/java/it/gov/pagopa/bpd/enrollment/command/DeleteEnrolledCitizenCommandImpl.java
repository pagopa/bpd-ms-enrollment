package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.core.command.BaseCommand;
import feign.FeignException;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.exception.PaymentInstrumenException;
import it.gov.pagopa.bpd.enrollment.exception.PaymentInstrumenWarnException;
import it.gov.pagopa.bpd.enrollment.exception.PaymentInstrumentDifferentChannelException;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import it.gov.pagopa.bpd.enrollment.service.WinningTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeleteEnrolledCitizenCommandImpl extends BaseCommand<Boolean> implements DeleteEnrolledCitizenCommand {

    private final String fiscalCode;
    private final String channel;

    private CitizenService citizenService;
    private PaymentInstrumentService paymentInstrumentService;
    private WinningTransactionService winningTransactionService;
    private final OffsetDateTime requestTimestamp = OffsetDateTime.now();

    public DeleteEnrolledCitizenCommandImpl(String fiscalCode, String channel) {
        this.fiscalCode = fiscalCode;
        this.channel = channel;
    }

    @Override
    protected Boolean doExecute() throws ExecutionException, InterruptedException {
        final PaymentInstrumentResource paymentInstrumentResource;

        try {
                try {
                    paymentInstrumentService.deleteByFiscalCode(fiscalCode, channel);
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

                winningTransactionService.deleteByFiscalCode(fiscalCode);

        } catch (Exception e) {
            if (logger.isWarnEnabled() && e.getCause() instanceof PaymentInstrumenWarnException) {
                logger.warn(e.getMessage(), e);
            } else if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }

            paymentInstrumentService.rollback(fiscalCode, requestTimestamp);

            if (e.getCause() instanceof PaymentInstrumenWarnException
                    && ((FeignException) e.getCause().getCause()).status() == 400) {
                throw new PaymentInstrumentDifferentChannelException(e.getMessage());
            }
            throw new RuntimeException("Uncapable to complete citizen deletion");
        }
        try {
            citizenService.delete(fiscalCode);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            CompletableFuture<Boolean> rollbackPiFuture = CompletableFuture.supplyAsync(() -> {
                paymentInstrumentService.rollback(fiscalCode, requestTimestamp);
                return true;
            });
            CompletableFuture<Boolean> rollbackTransactionFuture = CompletableFuture.supplyAsync(() -> {
                winningTransactionService.rollback(fiscalCode, requestTimestamp);
                return true;
            });
            rollbackPiFuture.get();
            rollbackTransactionFuture.get();
            throw new RuntimeException("Uncapable to complete citizen deletion", e);
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
