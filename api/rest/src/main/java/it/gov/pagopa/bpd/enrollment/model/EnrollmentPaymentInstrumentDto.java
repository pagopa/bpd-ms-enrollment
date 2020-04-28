package it.gov.pagopa.bpd.enrollment.model;

import it.gov.pagopa.bpd.common.model.validation.FutureOrPresentWithTolerance;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object (input) for {@link it.gov.pagopa.bpd.enrollment.controller.BpdEnrollmentController}
 */
@Data
public class EnrollmentPaymentInstrumentDto {

    @NotNull
    @FutureOrPresentWithTolerance(tolerance = 60)
    private OffsetDateTime activationDate;

}
