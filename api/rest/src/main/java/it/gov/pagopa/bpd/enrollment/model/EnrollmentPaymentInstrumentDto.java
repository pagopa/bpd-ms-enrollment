package it.gov.pagopa.bpd.enrollment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object (input) for {@link it.gov.pagopa.bpd.enrollment.controller.BpdEnrollmentController}
 */
@Data
public class EnrollmentPaymentInstrumentDto {

    @ApiModelProperty(value = "${swagger.enrollment.activationDate}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private OffsetDateTime activationDate;

}
