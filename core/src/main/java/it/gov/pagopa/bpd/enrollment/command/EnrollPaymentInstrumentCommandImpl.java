package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.exception.CitizenNotEnabledException;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import feign.FeignException;

import java.time.OffsetDateTime;

/**
 * @see it.gov.pagopa.bpd.enrollment.command.EnrollPaymentInstrumentCommand
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class EnrollPaymentInstrumentCommandImpl extends BaseCommand<PaymentInstrumentResource> implements EnrollPaymentInstrumentCommand {

    private final String hashPan;
    private final PaymentInstrumentDto paymentInstrumentDTO;

    @Autowired
    private CitizenService citizenService;
    @Autowired
    private PaymentInstrumentService paymentInstrumentService;


    public EnrollPaymentInstrumentCommandImpl(String hashPan, PaymentInstrumentDto paymentInstrumentDTO) {
        this.hashPan = hashPan;
        this.paymentInstrumentDTO = paymentInstrumentDTO;
    }


    @Override
    protected PaymentInstrumentResource doExecute() {
        final PaymentInstrumentResource paymentInstrumentResource;

        CitizenResource citizen = null;
        try{
            citizen = citizenService.findById(paymentInstrumentDTO.getFiscalCode());
        }catch(FeignException ex){
            if(ex.status()==404){
                throw new CitizenNotEnabledException(paymentInstrumentDTO.getFiscalCode());
            }else{
                throw ex;
            }
        }

        paymentInstrumentDTO.setActivationDate(OffsetDateTime.now());
        if (citizen.isEnabled()) {
            paymentInstrumentResource = paymentInstrumentService.update(hashPan, paymentInstrumentDTO);
        } else {
            throw new CitizenNotEnabledException(paymentInstrumentDTO.getFiscalCode());
        }

        return paymentInstrumentResource;
    }

}
