package it.gov.pagopa.bpd.enrollment.connector.citizen.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;


/**
 * Resource model (output) for {@link it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenRestClient}
 */
@Data
@EqualsAndHashCode(of = "fiscalCode", callSuper = false)
public class CitizenResource {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    private OffsetDateTime timestampTC;
    private boolean enabled;

}
