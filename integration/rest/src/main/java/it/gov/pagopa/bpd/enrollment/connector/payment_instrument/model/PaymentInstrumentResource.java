package it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

/**
 * Resource model (output) for {@link it.gov.pagopa.bpd.enrollment.connector.payment_instrument.PaymentInstrumentRestClient}
 */
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
