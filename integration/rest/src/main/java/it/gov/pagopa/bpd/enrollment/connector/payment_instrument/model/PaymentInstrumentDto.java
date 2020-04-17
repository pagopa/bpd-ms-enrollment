package it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PaymentInstrumentDto {

    private String fiscalCode;
    private OffsetDateTime activationDate;

}
