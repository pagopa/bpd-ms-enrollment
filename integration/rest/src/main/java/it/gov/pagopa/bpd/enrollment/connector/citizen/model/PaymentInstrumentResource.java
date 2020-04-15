package it.gov.pagopa.bpd.enrollment.connector.citizen.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(of = "hpan", callSuper = false)
public class PaymentInstrumentResource {

    private String hpan;
    private String fiscalCode;
    private OffsetDateTime activationDate;
    private OffsetDateTime cancellationDate;
    private Status status;

    public enum Status {
        ACTIVE, INACTIVE
    }

}
