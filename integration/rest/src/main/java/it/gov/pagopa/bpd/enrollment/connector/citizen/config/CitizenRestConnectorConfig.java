package it.gov.pagopa.bpd.enrollment.connector.citizen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "classpath:config/citizen/CitizenFindByIdRestConnector.properties",
        "classpath:config/citizen/CitizenUpdateRestConnector.properties"
})
public class CitizenRestConnectorConfig {
}
