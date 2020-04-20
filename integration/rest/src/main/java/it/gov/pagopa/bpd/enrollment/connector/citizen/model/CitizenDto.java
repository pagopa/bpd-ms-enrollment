package it.gov.pagopa.bpd.enrollment.connector.citizen.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class CitizenDto {

    @NotNull
    private OffsetDateTime timestampTC;

}
