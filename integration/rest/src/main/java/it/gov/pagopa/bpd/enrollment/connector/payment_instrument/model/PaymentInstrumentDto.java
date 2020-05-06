package it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model;

import it.gov.pagopa.bpd.common.model.validation.FutureOrPresentWithTolerance;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object (input) for {@link it.gov.pagopa.bpd.enrollment.connector.payment_instrument.PaymentInstrumentRestClient}
 */
@Data
public class PaymentInstrumentDto {

    @NotNull
//    @NotBlank
    @Size(max = 16)
    private String fiscalCode;
    @NotNull
    @FutureOrPresentWithTolerance(tolerance = 60)
    private OffsetDateTime activationDate;

}
