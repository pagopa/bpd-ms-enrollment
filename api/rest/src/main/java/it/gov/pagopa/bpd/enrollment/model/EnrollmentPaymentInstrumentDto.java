package it.gov.pagopa.bpd.enrollment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.gov.pagopa.bpd.common.util.Constants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object (input) for {@link it.gov.pagopa.bpd.enrollment.controller.BpdEnrollmentController}
 */
@Data
public class EnrollmentPaymentInstrumentDto {

    @NotNull
    @NotBlank
    @Size(min = 16, max = 16)
    @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
    private String fiscalCode;

}
