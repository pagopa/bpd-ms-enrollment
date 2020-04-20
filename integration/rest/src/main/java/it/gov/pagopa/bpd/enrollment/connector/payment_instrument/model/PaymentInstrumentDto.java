package it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class PaymentInstrumentDto {

    @NotBlank
    private String fiscalCode;
    @NotNull
    private OffsetDateTime activationDate;

}
