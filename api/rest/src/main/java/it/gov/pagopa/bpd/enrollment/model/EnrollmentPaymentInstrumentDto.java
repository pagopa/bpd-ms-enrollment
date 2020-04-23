package it.gov.pagopa.bpd.enrollment.model;

import it.gov.pagopa.bpd.common.model.validation.FutureOrPresentWithTolerance;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EnrollmentPaymentInstrumentDto {

    @FutureOrPresentWithTolerance(tolerance = 60)
    private OffsetDateTime activationDate;

}
