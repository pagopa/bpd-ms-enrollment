package it.gov.pagopa.bpd.enrollment.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.common.factory.ModelFactory;
import it.gov.pagopa.bpd.enrollment.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.enrollment.assembler.PaymentInstrumentResourceAssembler;
import it.gov.pagopa.bpd.enrollment.command.DeleteEnrolledCitizenCommand;
import it.gov.pagopa.bpd.enrollment.command.EnrollPaymentInstrumentCommand;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentCitizenResource;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentResource;
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
    private final PaymentInstrumentResourceAssembler paymentInstrumentResourceAssembler;
    private final CitizenResourceAssembler citizenResourceAssembler;
    private final ModelFactory<EnrollmentPaymentInstrumentDto, PaymentInstrumentDto> paymentInstrumentFactory;
    private final CitizenService citizenService;


    @Autowired
    public BpdEnrollmentControllerImpl(BeanFactory beanFactory,
                                       PaymentInstrumentResourceAssembler paymentInstrumentResourceAssembler, CitizenResourceAssembler citizenResourceAssembler, ModelFactory<EnrollmentPaymentInstrumentDto, PaymentInstrumentDto> paymentInstrumentFactory,
                                       CitizenService citizenService) {
        this.beanFactory = beanFactory;
        this.paymentInstrumentResourceAssembler = paymentInstrumentResourceAssembler;
        this.citizenResourceAssembler = citizenResourceAssembler;
        this.paymentInstrumentFactory = paymentInstrumentFactory;
        this.citizenService = citizenService;
    }


    @Override
    public EnrollmentCitizenResource enrollCitizenIO(String fiscalCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.enrollCitizenIO");
            logger.debug(String.format("fiscalCode = %s", fiscalCode));
        }

        return citizenResourceAssembler.toResource(citizenService.enroll(fiscalCode));
    }


    @Override
    public EnrollmentPaymentInstrumentResource enrollPaymentInstrumentIO(String hashPan, EnrollmentPaymentInstrumentDto request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.enrollPaymentInstrumentIO");
            logger.debug(String.format("hashPan = %s, request = %s", hashPan, request));
        }

        final PaymentInstrumentDto paymentInstrumentDTO = paymentInstrumentFactory.apply(request);
        PaymentInstrumentResource paymentInstrument = beanFactory.getBean(EnrollPaymentInstrumentCommand.class, hashPan, paymentInstrumentDTO)
                .execute();

        return paymentInstrumentResourceAssembler.toResource(paymentInstrument);
    }


    @Override
    public EnrollmentCitizenResource enrollCitizenHB(String fiscalCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.enrollCitizenHB");
            logger.debug(String.format("fiscalCode = [%s]", fiscalCode));
        }

        return citizenResourceAssembler.toResource(citizenService.enroll(fiscalCode));
    }


    @Override
    public EnrollmentPaymentInstrumentResource enrollPaymentInstrumentHB(String hashPan, EnrollmentPaymentInstrumentDto request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.enrollPaymentInstrumentHB");
            logger.debug(String.format("hashPan = %s, request = %s", hashPan, request));
        }

        final PaymentInstrumentDto paymentInstrumentDTO = paymentInstrumentFactory.apply(request);

        PaymentInstrumentResource paymentInstrument =  beanFactory.getBean(EnrollPaymentInstrumentCommand.class, hashPan, paymentInstrumentDTO)
                .execute();

        return paymentInstrumentResourceAssembler.toResource(paymentInstrument);

    }

    @Override
    public void deleteCitizen(String fiscalCode, String channel) throws Exception {

        if (logger.isDebugEnabled()) {
            logger.debug("BpdEnrollmentControllerImpl.deleteCitizen");
            logger.debug(String.format("fiscalCode = %s", fiscalCode));
        }

        if (!beanFactory.getBean(DeleteEnrolledCitizenCommand.class, fiscalCode, channel).execute()) {
            throw new Exception("Uncapable to complete citizen deletion");
        }

    }

}
