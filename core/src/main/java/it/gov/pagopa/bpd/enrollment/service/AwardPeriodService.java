package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.award_period.model.AwardPeriodResource;

import java.util.List;

/**
 * Service to manage the Business Logic related to Award Period
 */
public interface AwardPeriodService {

    List<AwardPeriodResource> findActives();

}