package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.core.command.Command;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;

public interface EnrollPaymentInstrumentCommand extends Command<PaymentInstrumentResource> {
}
