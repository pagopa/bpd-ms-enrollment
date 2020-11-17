package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.award_period.AwardPeriodRestClient;
import it.gov.pagopa.bpd.enrollment.connector.award_period.model.AwardPeriodResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AwardPeriodServiceImpl.class)
public class AwardPeriodServiceImplTest {

    @MockBean
    private AwardPeriodRestClient restClientMock;

    @Autowired
    private AwardPeriodServiceImpl awardPeriodService;


    @PostConstruct
    public void configureTests() {

        BDDMockito.doReturn(Collections.singletonList(getAwardPeriod()))
                .when(restClientMock)
                .findActives();
    }


    @Test
    public void findActives() {

        final List<AwardPeriodResource> found = awardPeriodService.findActives();

        verify(restClientMock, only()).findActives();
        verify(restClientMock, times(1)).findActives();
    }


    protected AwardPeriodResource getAwardPeriod() {

        AwardPeriodResource awardPeriod = new AwardPeriodResource();
        awardPeriod.setAwardPeriodId(1L);
        awardPeriod.setStartDate(OffsetDateTime.parse("2020-04-01T16:22:45.304Z").toLocalDate());
        awardPeriod.setEndDate(OffsetDateTime.parse("2020-04-01T16:22:45.304Z").toLocalDate());
        awardPeriod.setGracePeriod(5L);

        return awardPeriod;
    }

}