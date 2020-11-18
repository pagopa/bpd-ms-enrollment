package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.PaymentInstrumentRestClient;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PaymentInstrumentServiceImpl.class)
public class PaymentInstrumentServiceImplTest {

    @MockBean
    private PaymentInstrumentRestClient restClientMock;

    @Autowired
    private PaymentInstrumentServiceImpl paymentInstrumentService;


    @PostConstruct
    public void configureMock() {
        BDDMockito.when(restClientMock.update(Mockito.any(PaymentInstrumentDto.class), Mockito.any(String.class)))
                .thenAnswer(invocation -> {
                    PaymentInstrumentDto dtoArgument = invocation.getArgument(0, PaymentInstrumentDto.class);
                    PaymentInstrumentResource result = new PaymentInstrumentResource();
                    result.setHpan(invocation.getArgument(1));
                    result.setActivationDate(dtoArgument.getActivationDate());
                    result.setStatus(PaymentInstrumentResource.Status.ACTIVE);
                    result.setFiscalCode(dtoArgument.getFiscalCode());

                    return result;
                });
    }


    @Test
    public void update() {
        String hashPan = RandomStringUtils.randomNumeric(16);
        PaymentInstrumentDto dto = new PaymentInstrumentDto();
        dto.setFiscalCode(RandomStringUtils.randomAlphanumeric(16));
        dto.setActivationDate(OffsetDateTime.now());
        dto.setChannel("channel");

        PaymentInstrumentResource updated = paymentInstrumentService.update(hashPan, dto);

        verify(restClientMock, only()).update(eq(dto), eq(hashPan));
        verify(restClientMock, times(1)).update(eq(dto), eq(hashPan));
        assertEquals(hashPan, updated.getHpan());
        assertEquals(dto.getActivationDate(), updated.getActivationDate());
        assertEquals(PaymentInstrumentResource.Status.ACTIVE, updated.getStatus());
        assertEquals(dto.getFiscalCode(), updated.getFiscalCode());
    }

    @Test
    public void delete() {
        String fiscalCode = "testFiscalCode";
        String channel = "channel";
        paymentInstrumentService.deleteByFiscalCode(fiscalCode, channel);

        verify(restClientMock, only()).deleteByFiscalCode(eq(fiscalCode), eq(channel));
        verify(restClientMock, times(1)).deleteByFiscalCode(eq(fiscalCode), eq(channel));
    }

    @Test
    public void rollback() {
        String fiscalCode = "testFiscalCode";
        OffsetDateTime requestTimestamp = OffsetDateTime.now();
        paymentInstrumentService.rollback(fiscalCode, requestTimestamp);

        verify(restClientMock, only()).rollback(eq(fiscalCode), eq(requestTimestamp));
        verify(restClientMock, times(1)).rollback(eq(fiscalCode), eq(requestTimestamp));
    }

}