package it.gov.pagopa.bpd.enrollment.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.enrollment.command.EnrollPaymentInstrumentCommand;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.factory.ModelFactory;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentDto;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BpdEnrollmentControllerImpl extends StatelessController implements BpdEnrollmentController {

    private final BeanFactory beanFactory;
    private final ModelFactory<EnrollmentDto, PaymentInstrumentDto> paymentInstrumentFactory;


    @Autowired
    public BpdEnrollmentControllerImpl(BeanFactory beanFactory,
                                       ModelFactory<EnrollmentDto, PaymentInstrumentDto> paymentInstrumentFactory) {
        this.beanFactory = beanFactory;
        this.paymentInstrumentFactory = paymentInstrumentFactory;
    }


    @Override
    public PaymentInstrumentResource enrollPaymentInstrumentIO(String hashPan, EnrollmentDto request) {
        logger.info(request.toString());

        String fiscalCode = "test";//FIXME: get fiscal code from token
        final PaymentInstrumentDto paymentInstrumentDTO = paymentInstrumentFactory.createModel(request);
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
