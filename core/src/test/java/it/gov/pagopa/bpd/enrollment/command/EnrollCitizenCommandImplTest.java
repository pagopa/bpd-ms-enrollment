package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.async.util.AsyncUtils;
import eu.sia.meda.core.model.ApplicationContext;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.exception.CitizenNotEnabledException;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EnrollPaymentInstrumentCommandImpl.class, ApplicationContext.class, AsyncUtils.class})
public class EnrollCitizenCommandImplTest {

    private static final OffsetDateTime CURRENT_DATE_TIME = OffsetDateTime.now();
    private static final String FISCAL_CODE = "test";
    private static final String HASH_PAN = "hashPan";
    private static final String FISCAL_CODE_ERROR = "test-error";

    @MockBean
    private CitizenService citizenService;
    @MockBean
    private PaymentInstrumentService paymentInstrumentService;

    @Autowired
    private BeanFactory beanFactory;


    @PostConstruct
    public void configureMock() {
        when(citizenService.findById(any(String.class)))
                .thenAnswer(invocation -> {
                    if(FISCAL_CODE.equals(invocation.getArgument(0))
                            || FISCAL_CODE_ERROR.equals(invocation.getArgument(0))) {
                        CitizenResource result = new CitizenResource();
                        result.setFiscalCode(invocation.getArgument(0));
                        result.setTimestampTC(OffsetDateTime.now());
                        result.setEnabled(FISCAL_CODE.equals(result.getFiscalCode()));

                        return result;
                    }else{
                        throw new CitizenNotEnabledException(invocation.getArgument(0));
                    }
                });

        when(paymentInstrumentService.find(any(String.class))).thenAnswer(invocation -> {
            PaymentInstrumentResource pi = new PaymentInstrumentResource();
            pi.setHpan(invocation.getArgument(0,String.class));
            pi.setFiscalCode(FISCAL_CODE);

            return pi;
        });

        when(paymentInstrumentService.update(any(String.class), any(PaymentInstrumentDto.class)))
                .thenAnswer(invocation -> {
                    final PaymentInstrumentDto dtoArgument = invocation.getArgument(1, PaymentInstrumentDto.class);
                    final PaymentInstrumentResource result = new PaymentInstrumentResource();
                    result.setHpan(invocation.getArgument(0));
                    result.setActivationDate(dtoArgument.getActivationDate());
                    result.setStatus(PaymentInstrumentResource.Status.ACTIVE);
                    result.setFiscalCode(dtoArgument.getFiscalCode());

                    return result;
                });
    }


    @Test
    public void doExecuteOK() {
        final PaymentInstrumentDto dto = new PaymentInstrumentDto();
        dto.setFiscalCode(FISCAL_CODE);

        EnrollPaymentInstrumentCommandImpl enrollPaymentInstrumentCommand =
                beanFactory.getBean(EnrollPaymentInstrumentCommandImpl.class, HASH_PAN, dto);
        final PaymentInstrumentResource enrolled = enrollPaymentInstrumentCommand.doExecute();

        verify(citizenService, only()).findById(eq(dto.getFiscalCode()));
        verify(citizenService, times(1)).findById(eq(dto.getFiscalCode()));
        //verify(paymentInstrumentService, only()).update(eq(HASH_PAN), eq(dto));
        verify(paymentInstrumentService, times(1)).update(eq(HASH_PAN), eq(dto));
        assertEquals(HASH_PAN, enrolled.getHpan());
        assertEquals(CURRENT_DATE_TIME.truncatedTo(ChronoUnit.DAYS), enrolled.getActivationDate().truncatedTo(ChronoUnit.DAYS));
        assertEquals(PaymentInstrumentResource.Status.ACTIVE, enrolled.getStatus());
        assertEquals(dto.getFiscalCode(), enrolled.getFiscalCode());
    }

    @Test(expected = CitizenNotEnabledException.class)
    public void doExecuteCitizenDisabled() {
        final PaymentInstrumentDto dto = new PaymentInstrumentDto();
        dto.setFiscalCode("fiscal-code-disabled");
        dto.setActivationDate(CURRENT_DATE_TIME);

        EnrollPaymentInstrumentCommandImpl enrollPaymentInstrumentCommand =
                beanFactory.getBean(EnrollPaymentInstrumentCommandImpl.class, HASH_PAN, dto);

        try {
            enrollPaymentInstrumentCommand.doExecute();

        } finally {
            verify(citizenService, only()).findById(eq(dto.getFiscalCode()));
            verify(citizenService, times(1)).findById(eq(dto.getFiscalCode()));
            verify(paymentInstrumentService, Mockito.never()).update(eq(HASH_PAN), eq(dto));
        }
    }

}