package it.gov.pagopa.bpd.enrollment.api.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.enrollment.command.EnrollPaymentInstrumentCommand;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BpdEnrollmentControllerImpl extends StatelessController implements BpdEnrollmentController {

    private final BeanFactory beanFactory;


    @Autowired
    public BpdEnrollmentControllerImpl(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public PaymentInstrumentResource enrollPaymentInstrumentIO(String hashPan, PaymentInstrumentDTO request) {
        logger.info(request.toString());

        PaymentInstrumentResource result = null;
        try {
            result = beanFactory.getBean(EnrollPaymentInstrumentCommand.class, hashPan, request).execute();
        } catch (Exception e) {
            logger.error("Something gone wrong", e);
        }

        return result;
    }

}
