package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.async.util.AsyncUtils;
import eu.sia.meda.core.model.ApplicationContext;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.exception.CitizenNotEnabledException;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EnrollPaymentInstrumentCommandImpl.class, ApplicationContext.class, AsyncUtils.class})
public class EnrollCitizenCommandImplTest {

    private static final OffsetDateTime CURRENT_DATE_TIME = OffsetDateTime.now();
    private static final String FISCAL_CODE = "test";
    private static final String HASH_PAN = "hashPan";

    @MockBean
    private CitizenService citizenService;
    @MockBean
    private PaymentInstrumentService paymentInstrumentService;

    @Autowired
    private BeanFactory beanFactory;


    @PostConstruct
    public void configureMock() {
        BDDMockito.when(citizenService.findById(Mockito.any(String.class)))
                .thenAnswer(invocation -> {
                    CitizenResource result = new CitizenResource();
                    result.setFiscalCode(invocation.getArgument(0));
                    result.setTimestampTC(OffsetDateTime.now());
                    result.setEnabled(FISCAL_CODE.equals(result.getFiscalCode()));

                    return result;
                });

        BDDMockito.when(paymentInstrumentService.update(Mockito.any(String.class), Mockito.any(PaymentInstrumentDTO.class)))
                .thenAnswer(invocation -> {
                    final PaymentInstrumentDTO dtoArgument = invocation.getArgument(1, PaymentInstrumentDTO.class);
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
        final PaymentInstrumentDTO dto = new PaymentInstrumentDTO();
        dto.setFiscalCode(FISCAL_CODE);
        dto.setActivationDate(CURRENT_DATE_TIME);

        EnrollPaymentInstrumentCommandImpl enrollPaymentInstrumentCommand =
                beanFactory.getBean(EnrollPaymentInstrumentCommandImpl.class, HASH_PAN, dto);
        final PaymentInstrumentResource enrolled = enrollPaymentInstrumentCommand.doExecute();

        BDDMockito.verify(citizenService, Mockito.only()).findById(Mockito.eq(dto.getFiscalCode()));
        BDDMockito.verify(citizenService, Mockito.times(1)).findById(Mockito.eq(dto.getFiscalCode()));
        BDDMockito.verify(paymentInstrumentService, Mockito.only()).update(Mockito.eq(HASH_PAN), Mockito.eq(dto));
        BDDMockito.verify(paymentInstrumentService, Mockito.times(1)).update(Mockito.eq(HASH_PAN), Mockito.eq(dto));
        Assert.assertEquals(HASH_PAN, enrolled.getHpan());
        Assert.assertEquals(CURRENT_DATE_TIME, enrolled.getActivationDate());
        Assert.assertEquals(PaymentInstrumentResource.Status.ACTIVE, enrolled.getStatus());
        Assert.assertEquals(dto.getFiscalCode(), enrolled.getFiscalCode());
    }


    @Test(expected = CitizenNotEnabledException.class)
    public void doExecuteCitizenDisabled() {
        final PaymentInstrumentDTO dto = new PaymentInstrumentDTO();
        dto.setFiscalCode("fiscal-code-disabled");
        dto.setActivationDate(CURRENT_DATE_TIME);

        EnrollPaymentInstrumentCommandImpl enrollPaymentInstrumentCommand =
                beanFactory.getBean(EnrollPaymentInstrumentCommandImpl.class, HASH_PAN, dto);

        try {
            enrollPaymentInstrumentCommand.doExecute();

        } finally {
            BDDMockito.verify(citizenService, Mockito.only()).findById(Mockito.eq(dto.getFiscalCode()));
            BDDMockito.verify(citizenService, Mockito.times(1)).findById(Mockito.eq(dto.getFiscalCode()));
            BDDMockito.verify(paymentInstrumentService, Mockito.never()).update(Mockito.eq(HASH_PAN), Mockito.eq(dto));
        }
    }

}