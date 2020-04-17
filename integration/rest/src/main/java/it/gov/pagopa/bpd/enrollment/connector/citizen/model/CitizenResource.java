package it.gov.pagopa.bpd.enrollment.connector.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;


@Data
@EqualsAndHashCode(of = "fiscalCode", callSuper = false)
public class CitizenResource {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    @JsonProperty("timestampTc")
    private OffsetDateTime timestampTC;
    private boolean enabled;

}
