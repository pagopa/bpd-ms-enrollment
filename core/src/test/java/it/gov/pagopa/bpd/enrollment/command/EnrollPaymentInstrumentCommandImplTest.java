package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.async.util.AsyncUtils;
import eu.sia.meda.core.model.ApplicationContext;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DeleteEnrolledCitizenCommandImpl.class, ApplicationContext.class, AsyncUtils.class})
public class EnrollPaymentInstrumentCommandImplTest {

    @MockBean
    private CitizenService citizenService;
    @MockBean
    private PaymentInstrumentService paymentInstrumentService;

    @Autowired
    private BeanFactory beanFactory;

    private static final String FISCAL_CODE = "test";

    @Test
    public void doExecuteOK() {

        BDDMockito.doNothing().when(citizenService).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.doNothing().when(paymentInstrumentService).deleteByFiscalCode(Mockito.eq(FISCAL_CODE));

        DeleteEnrolledCitizenCommandImpl deleteEnrolledCitizenCommand =
                beanFactory.getBean(DeleteEnrolledCitizenCommandImpl.class, FISCAL_CODE);
        Boolean result = deleteEnrolledCitizenCommand.doExecute();
        Assert.assertTrue(result);

        BDDMockito.verify(citizenService, Mockito.times(1)).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.verify(paymentInstrumentService, Mockito.times(1))
                .deleteByFiscalCode(Mockito.eq(FISCAL_CODE));

    }

    @Test
    public void doExecute_CitizenKO() {

        BDDMockito.doAnswer(invocationOnMock -> {
            throw new Exception();
        }).when(citizenService).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.doNothing().when(paymentInstrumentService).deleteByFiscalCode(Mockito.eq(FISCAL_CODE));

        DeleteEnrolledCitizenCommandImpl deleteEnrolledCitizenCommand =
                beanFactory.getBean(DeleteEnrolledCitizenCommandImpl.class, FISCAL_CODE);
        Boolean result = deleteEnrolledCitizenCommand.doExecute();
        Assert.assertFalse(result);

        BDDMockito.verify(citizenService, Mockito.times(1)).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.verifyZeroInteractions(paymentInstrumentService);

    }

    @Test
    public void doExecute_PaymentInstrumentKO() {

        BDDMockito.doNothing().when(citizenService).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.doAnswer(invocationOnMock -> {
            throw new Exception();
        }).when(paymentInstrumentService).deleteByFiscalCode(Mockito.eq(FISCAL_CODE));

        DeleteEnrolledCitizenCommandImpl deleteEnrolledCitizenCommand =
                beanFactory.getBean(DeleteEnrolledCitizenCommandImpl.class, FISCAL_CODE);
        Boolean result = deleteEnrolledCitizenCommand.doExecute();
        Assert.assertFalse(result);

        BDDMockito.verify(citizenService, Mockito.times(1)).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.verify(paymentInstrumentService, Mockito.times(1))
                .deleteByFiscalCode(Mockito.eq(FISCAL_CODE));

    }

}