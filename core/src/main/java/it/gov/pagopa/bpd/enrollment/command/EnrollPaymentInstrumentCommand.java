package it.gov.pagopa.bpd.enrollment.command;

import eu.sia.meda.core.command.Command;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;

/**
 * Command pattern to orchestrate more than one service in order to execute the Enrollment Payment Instrument process
 */
public interface EnrollPaymentInstrumentCommand extends Command<PaymentInstrumentResource> {
}
