package it.gov.pagopa.bpd.enrollment.api.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.enrollment.api.factory.ModelFactory;
import it.gov.pagopa.bpd.enrollment.api.model.EnrollmentDTO;
import it.gov.pagopa.bpd.enrollment.command.EnrollPaymentInstrumentCommand;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BpdEnrollmentControllerImpl extends StatelessController implements BpdEnrollmentController {

    private final BeanFactory beanFactory;
    private final ModelFactory<EnrollmentDTO, PaymentInstrumentDTO> paymentInstrumentFactory;


    @Autowired
    public BpdEnrollmentControllerImpl(BeanFactory beanFactory,
                                       ModelFactory<EnrollmentDTO, PaymentInstrumentDTO> paymentInstrumentFactory) {
        this.beanFactory = beanFactory;
        this.paymentInstrumentFactory = paymentInstrumentFactory;
    }


    @Override
    public PaymentInstrumentResource enrollPaymentInstrumentIO(String hashPan, EnrollmentDTO request) {
        logger.info(request.toString());

        String fiscalCode = "test";//FIXME: get fiscal code from token
        final PaymentInstrumentDTO paymentInstrumentDTO = paymentInstrumentFactory.createModel(request);
        paymentInstrumentDTO.setFiscalCode(fiscalCode);

        PaymentInstrumentResource result = null;
        try {
            result = beanFactory.getBean(EnrollPaymentInstrumentCommand.class, hashPan, paymentInstrumentDTO).execute();
        } catch (Exception e) {
            logger.error("Something gone wrong", e);
        }

        return result;
    }

}
