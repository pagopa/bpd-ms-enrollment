package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.async.util.AsyncUtils;
import eu.sia.meda.core.model.ApplicationContext;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import it.gov.pagopa.bpd.enrollment.service.WinningTransactionService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DeleteEnrolledCitizenCommandImpl.class, ApplicationContext.class, AsyncUtils.class})
public class EnrollPaymentInstrumentCommandImplTest {

    @MockBean
    private CitizenService citizenService;
    @MockBean
    private PaymentInstrumentService paymentInstrumentService;
    @MockBean
    private WinningTransactionService winningTransactionService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private BeanFactory beanFactory;

    private static final String FISCAL_CODE = "test";
    private static final String CHANNEL = "channel";

    @Test
    public void doExecuteOK() throws ExecutionException, InterruptedException {

        BDDMockito.doNothing().when(citizenService).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.doNothing().when(paymentInstrumentService).deleteByFiscalCode(Mockito.eq(FISCAL_CODE), Mockito.eq(CHANNEL));
        BDDMockito.doNothing().when(winningTransactionService).deleteByFiscalCode(Mockito.eq(FISCAL_CODE));

        DeleteEnrolledCitizenCommandImpl deleteEnrolledCitizenCommand =
                beanFactory.getBean(DeleteEnrolledCitizenCommandImpl.class, FISCAL_CODE, CHANNEL);
        Boolean result = deleteEnrolledCitizenCommand.doExecute();
        Assert.assertTrue(result);

        BDDMockito.verify(citizenService, Mockito.times(1)).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.verify(paymentInstrumentService, Mockito.times(1))
                .deleteByFiscalCode(Mockito.eq(FISCAL_CODE), Mockito.eq(CHANNEL));
        BDDMockito.verify(winningTransactionService, Mockito.times(1)).deleteByFiscalCode(Mockito.eq(FISCAL_CODE));

    }

    @Test
    public void doExecute_CitizenKO() throws ExecutionException, InterruptedException {

        BDDMockito.doAnswer(invocationOnMock -> {
            throw new Exception();
        }).when(citizenService).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.doNothing().when(paymentInstrumentService).deleteByFiscalCode(Mockito.eq(FISCAL_CODE), Mockito.eq(CHANNEL));

        DeleteEnrolledCitizenCommandImpl deleteEnrolledCitizenCommand =
                beanFactory.getBean(DeleteEnrolledCitizenCommandImpl.class, FISCAL_CODE, CHANNEL);

        expectedException.expect(Exception.class);
        deleteEnrolledCitizenCommand.doExecute();

        BDDMockito.verify(citizenService, Mockito.times(1)).delete(Mockito.eq(FISCAL_CODE));
    }

    @Test
    public void doExecute_PaymentInstrumentKO() throws ExecutionException, InterruptedException {

        BDDMockito.doNothing().when(citizenService).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.doAnswer(invocationOnMock -> {
            throw new Exception();
        }).when(paymentInstrumentService).deleteByFiscalCode(Mockito.eq(FISCAL_CODE), Mockito.eq(CHANNEL));

        DeleteEnrolledCitizenCommandImpl deleteEnrolledCitizenCommand =
                beanFactory.getBean(DeleteEnrolledCitizenCommandImpl.class, FISCAL_CODE, CHANNEL);


        expectedException.expect(Exception.class);
        deleteEnrolledCitizenCommand.doExecute();

        BDDMockito.verify(paymentInstrumentService, Mockito.times(1))
                .deleteByFiscalCode(Mockito.eq(FISCAL_CODE), Mockito.eq(CHANNEL));

    }

    @Test
    public void doExecute_WinningTransactionKO() throws ExecutionException, InterruptedException {

        BDDMockito.doAnswer(invocationOnMock -> {
            throw new Exception();
        }).when(citizenService).delete(Mockito.eq(FISCAL_CODE));
        BDDMockito.doNothing().when(winningTransactionService).deleteByFiscalCode(Mockito.eq(FISCAL_CODE));

        DeleteEnrolledCitizenCommandImpl deleteEnrolledCitizenCommand =
                beanFactory.getBean(DeleteEnrolledCitizenCommandImpl.class, FISCAL_CODE, CHANNEL);


        expectedException.expect(Exception.class);
        deleteEnrolledCitizenCommand.doExecute();

        BDDMockito.verify(winningTransactionService, Mockito.times(1)).deleteByFiscalCode(Mockito.eq(FISCAL_CODE));
    }

}