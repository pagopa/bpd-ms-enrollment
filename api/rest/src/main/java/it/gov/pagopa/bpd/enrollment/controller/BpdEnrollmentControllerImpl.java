package it.gov.pagopa.bpd.enrollment.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.common.factory.ModelFactory;
import it.gov.pagopa.bpd.enrollment.command.EnrollPaymentInstrumentCommand;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @see BpdEnrollmentController
 */
@RestController
class BpdEnrollmentControllerImpl extends StatelessController implements BpdEnrollmentController {

    private final BeanFactory beanFactory;
    private final ModelFactory<EnrollmentPaymentInstrumentDto, PaymentInstrumentDto> paymentInstrumentFactory;
    private final CitizenService citizenService;


    @Autowired
    public BpdEnrollmentControllerImpl(BeanFactory beanFactory,
                                       ModelFactory<EnrollmentPaymentInstrumentDto, PaymentInstrumentDto> paymentInstrumentFactory,
                                       CitizenService citizenService) {
        this.beanFactory = beanFactory;
        this.paymentInstrumentFactory = paymentInstrumentFactory;
        this.citizenService = citizenService;
    }


    @Override
    public CitizenResource enrollCitizenIO(String fiscalCode, CitizenDto request) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.enrollCitizenIO");
            logger.debug(String.format("fiscalCode = %s, request = %s", fiscalCode, request));
        }

        return citizenService.update(fiscalCode, request);
    }


    @Override
    public PaymentInstrumentResource enrollPaymentInstrumentIO(String hashPan, String fiscalCode, EnrollmentPaymentInstrumentDto request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.enrollPaymentInstrumentIO");
            logger.debug(String.format("hashPan = %s, fiscalCode = %s, request = %s", hashPan, fiscalCode, request));
        }

        final PaymentInstrumentDto paymentInstrumentDTO = paymentInstrumentFactory.apply(request);
        paymentInstrumentDTO.setFiscalCode(fiscalCode);

        return beanFactory.getBean(EnrollPaymentInstrumentCommand.class, hashPan, paymentInstrumentDTO)
                .execute();
    }


    @Override
    public CitizenResource enrollCitizenHB(String fiscalCode, CitizenDto request) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.enrollCitizenHB");
            logger.debug(String.format("fiscalCode = [%s], request = [%s]", fiscalCode, request));
        }

        return citizenService.update(fiscalCode, request);
    }


    @Override
    public PaymentInstrumentResource enrollPaymentInstrumentHB(String hashPan, PaymentInstrumentDto request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.enrollPaymentInstrumentHB");
            logger.debug(String.format("hashPan = %s, request = %s", hashPan, request));
        }

        return beanFactory.getBean(EnrollPaymentInstrumentCommand.class, hashPan, request)
                .execute();
    }

}
