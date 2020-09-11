package it.gov.pagopa.bpd.enrollment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EnrollmentPaymentInstrumentResource {

    @ApiModelProperty(value = "${swagger.enrollment.activationDate}", required = true)
    @JsonProperty(required = true)
    private OffsetDateTime activationDate;


}
