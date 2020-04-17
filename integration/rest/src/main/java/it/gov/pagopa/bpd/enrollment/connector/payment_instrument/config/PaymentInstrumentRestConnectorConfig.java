package it.gov.pagopa.bpd.enrollment.connector.payment_instrument.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/payment_instrument/PaymentInstrumentRestConnector.properties")
public class PaymentInstrumentRestConnectorConfig {
}
