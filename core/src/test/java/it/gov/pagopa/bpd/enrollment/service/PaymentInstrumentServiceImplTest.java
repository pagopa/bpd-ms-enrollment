package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.PaymentInstrumentRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.junit.Assert;
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

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PaymentInstrumentServiceImpl.class)
public class PaymentInstrumentServiceImplTest {

    private static final OffsetDateTime CURRENT_DATE_TIME = OffsetDateTime.now();
    private static final String FISCAL_CODE = "fiscalCode";

    @MockBean
    private PaymentInstrumentRestClient restClientMock;

    @Autowired
    private PaymentInstrumentServiceImpl paymentInstrumentService;


    @PostConstruct
    public void configureMock() {
        BDDMockito.when(restClientMock.update(Mockito.any(String.class), Mockito.any(PaymentInstrumentDto.class)))
                .thenAnswer(invocation -> {
                    final PaymentInstrumentDto dtoArgument = invocation.getArgument(1, PaymentInstrumentDto.class);
                    final PaymentInstrumentResource result = new PaymentInstrumentResource();
                    result.setHpan(invocation.getArgument(0));
                    result.setActivationDate(dtoArgument.getActivationDate());
                    result.setStatus(PaymentInstrumentResource.Status.ACTIVE);
                    result.setFiscalCode(FISCAL_CODE);

                    return result;
                });
    }


    @Test
    public void update() {
        final String hashPan = "hpan";
        final PaymentInstrumentDto dto = new PaymentInstrumentDto();
        dto.setActivationDate(CURRENT_DATE_TIME);

        final PaymentInstrumentResource updated = paymentInstrumentService.update(hashPan, dto);

        BDDMockito.verify(restClientMock, Mockito.times(1)).update(Mockito.eq(hashPan), Mockito.eq(dto));
        BDDMockito.verify(restClientMock, Mockito.only()).update(Mockito.eq(hashPan), Mockito.eq(dto));
        Assert.assertEquals(hashPan, updated.getHpan());
        Assert.assertEquals(CURRENT_DATE_TIME, updated.getActivationDate());
        Assert.assertEquals(PaymentInstrumentResource.Status.ACTIVE, updated.getStatus());
        Assert.assertEquals(FISCAL_CODE, updated.getFiscalCode());
    }

}