package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
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
@ContextConfiguration(classes = CitizenServiceImpl.class)
public class CitizenServiceImplTest {

    @MockBean
    private CitizenRestClient restClientMock;

    @Autowired
    private CitizenServiceImpl citizenService;


    @PostConstruct
    public void configureMock() {
        BDDMockito.when(restClientMock.findById(Mockito.any(String.class)))
                .thenAnswer(invocation -> {
                    final CitizenResource result = new CitizenResource();
                    result.setFiscalCode(invocation.getArgument(0));
                    result.setTimestampTC(OffsetDateTime.now());

                    return result;
                });
    }


    @Test
    public void findById() {
        String fiscalCode = "fiscalCode";

        final CitizenResource found = citizenService.findById(fiscalCode);

        BDDMockito.verify(restClientMock, Mockito.times(1)).findById(Mockito.eq(fiscalCode));
        BDDMockito.verify(restClientMock, Mockito.only()).findById(Mockito.eq(fiscalCode));
        Assert.assertEquals(fiscalCode, found.getFiscalCode());
        Assert.assertNotNull(found.getTimestampTC());
    }
}