package it.gov.pagopa.bpd.enrollment.connector.citizen.model;

import it.gov.pagopa.bpd.common.model.validation.FutureOrPresentWithTolerance;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class CitizenDto {

    @NotNull
    @FutureOrPresentWithTolerance(tolerance = 60)
    private OffsetDateTime timestampTC;

}
