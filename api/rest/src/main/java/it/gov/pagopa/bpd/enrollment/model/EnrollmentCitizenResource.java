package it.gov.pagopa.bpd.enrollment.model;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Data Transfer Object (input) for {@link it.gov.pagopa.bpd.enrollment.controller.BpdEnrollmentController}
 */
@Data
public class EnrollmentCitizenResource {

    @ApiModelProperty(value = "${swagger.enrollment.fiscalCode}", required = true)
    private String fiscalCode;

    @ApiModelProperty(value = "${swagger.enrollment.payoffInstr}", required = true)
    private String payoffInstr;

    @ApiModelProperty(value = "${swagger.enrollment.payoffInstrType}", required = true)
    private String payoffInstrType;

    @ApiModelProperty(value = "${swagger.enrollment.timestampTC}", required = true)
    private OffsetDateTime timestampTC;

    @ApiModelProperty(value = "${swagger.enrollment.enabled}", required = true)
    private boolean enabled;

    @ApiModelProperty(value = "${swagger.enrollment.optInStatus}", required = false)
    private CitizenDto.OptInStatus optInStatus;
}
