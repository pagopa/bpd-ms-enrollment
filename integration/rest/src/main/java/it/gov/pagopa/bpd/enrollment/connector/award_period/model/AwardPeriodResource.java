package it.gov.pagopa.bpd.enrollment.connector.award_period.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Resource model (output) for {@link it.gov.pagopa.bpd.enrollment.connector.award_period.AwardPeriodRestClient}
 */
@Data
@EqualsAndHashCode(of = "awardPeriodId", callSuper = false)
public class AwardPeriodResource {

    private Long awardPeriodId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long minTransactionNumber;

    private Long maxAmount;

    private Long minPosition;

    private Long maxTransactionCashback;

    private Long maxPeriodCashback;

    private Long cashbackPercentage;

    private Long gracePeriod;

    private String status;

    private Long maxTransactionEvaluated;
}
