package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.PaymentInstrumentRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
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
        BDDMockito.when(restClientMock.update(Mockito.any(String.class), Mockito.any(PaymentInstrumentDTO.class)))
                .thenAnswer((Answer<PaymentInstrumentResource>) invocation -> {
                    final PaymentInstrumentDTO dtoArgument = invocation.getArgument(1, PaymentInstrumentDTO.class);
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
        final String hashPanValue = "hpan";
        final PaymentInstrumentDTO dto = new PaymentInstrumentDTO();
        dto.setActivationDate(CURRENT_DATE_TIME);

        final PaymentInstrumentResource updated = paymentInstrumentService.update(hashPanValue, dto);

        BDDMockito.verify(restClientMock, Mockito.times(1)).update(Mockito.eq(hashPanValue), Mockito.eq(dto));
        Assert.assertEquals(hashPanValue, updated.getHpan());
        Assert.assertEquals(CURRENT_DATE_TIME, updated.getActivationDate());
        Assert.assertEquals(PaymentInstrumentResource.Status.ACTIVE, updated.getStatus());
        Assert.assertEquals(FISCAL_CODE, updated.getFiscalCode());
    }

}