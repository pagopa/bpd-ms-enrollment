package it.gov.pagopa.bpd.enrollment.connector.citizen.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(of = "hpan", callSuper = false)
public class PaymentInstrumentResource {

    private String hpan;
    private String fiscalCode;
    private ZonedDateTime activationDate;
    private ZonedDateTime cancellationDate;
    private Status Status;

    public enum Status {
        ACTIVE, INACTIVE
    }

}
