package it.gov.pagopa.bpd.enrollment.api.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EnrollmentDTO {

    private OffsetDateTime activationDate;

}
