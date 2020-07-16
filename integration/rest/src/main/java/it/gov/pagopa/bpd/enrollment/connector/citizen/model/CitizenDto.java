package it.gov.pagopa.bpd.enrollment.connector.citizen.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object (input) for {@link it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenRestClient}
 */
@Data
public class CitizenDto {

    @NotNull
    private OffsetDateTime timestampTC;

}
