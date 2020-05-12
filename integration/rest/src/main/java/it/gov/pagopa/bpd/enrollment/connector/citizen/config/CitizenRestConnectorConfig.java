package it.gov.pagopa.bpd.enrollment.connector.citizen.config;

import it.gov.pagopa.bpd.common.connector.config.RestConnectorConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestConnectorConfig.class)
@PropertySource({
        "classpath:config/citizen/CitizenFindByIdRestConnector.properties",
        "classpath:config/citizen/CitizenUpdateRestConnector.properties",
        "classpath:config/citizen/rest-client.properties"
})
public class CitizenRestConnectorConfig {

}
