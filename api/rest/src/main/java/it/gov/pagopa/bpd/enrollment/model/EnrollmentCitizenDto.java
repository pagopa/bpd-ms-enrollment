package it.gov.pagopa.bpd.enrollment.model;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Data Transfer Object (input) for {@link it.gov.pagopa.bpd.enrollment.controller.BpdEnrollmentController}
 */
@Data
public class EnrollmentCitizenDto {

  @ApiModelProperty(value = "${swagger.enrollment.optInStatus}", required = false)
  private CitizenDto.OptInStatus optInStatus;

}
