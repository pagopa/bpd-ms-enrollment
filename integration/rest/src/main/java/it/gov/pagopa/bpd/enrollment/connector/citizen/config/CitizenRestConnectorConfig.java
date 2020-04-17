package it.gov.pagopa.bpd.enrollment.connector.citizen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/citizen/CitizenRestConnector.properties")
public class CitizenRestConnectorConfig {
}
