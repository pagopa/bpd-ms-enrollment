package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.award_period.AwardPeriodRestClient;
import it.gov.pagopa.bpd.enrollment.connector.award_period.model.AwardPeriodResource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @see it.gov.pagopa.bpd.enrollment.service.AwardPeriodService
 */
@Service
public class AwardPeriodServiceImpl implements AwardPeriodService{

    private final AwardPeriodRestClient restClient;

    public AwardPeriodServiceImpl(AwardPeriodRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<AwardPeriodResource> findActives() {
        return restClient.findActives();
    }

}
