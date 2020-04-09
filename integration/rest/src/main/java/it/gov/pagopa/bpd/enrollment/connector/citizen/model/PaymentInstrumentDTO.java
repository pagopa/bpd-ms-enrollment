package it.gov.pagopa.bpd.enrollment.connector.citizen.model;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class PaymentInstrumentDTO {

    private ZonedDateTime activationDate;

}
