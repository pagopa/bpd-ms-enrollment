package it.gov.pagopa.bpd.enrollment.connector.citizen.config;

import it.gov.pagopa.bpd.common.connector.config.RestConnectorConfig;
import it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestConnectorConfig.class)
@EnableFeignClients(clients = CitizenRestClient.class)
@PropertySource("classpath:config/citizen/rest-client.properties")
public class CitizenRestConnectorConfig {

}
