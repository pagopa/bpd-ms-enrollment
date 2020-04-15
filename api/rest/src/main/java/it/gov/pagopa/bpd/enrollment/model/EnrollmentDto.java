package it.gov.pagopa.bpd.enrollment.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class EnrollmentDto {

    @NotNull
    private OffsetDateTime activationDate;

}
