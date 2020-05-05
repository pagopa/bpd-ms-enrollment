package it.gov.pagopa.bpd.enrollment.connector.citizen.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableFeignClients(basePackages = "it.gov.pagopa.bpd.enrollment.connector.citizen")
@PropertySource({
        "classpath:config/citizen/CitizenFindByIdRestConnector.properties",
        "classpath:config/citizen/CitizenUpdateRestConnector.properties",
        "classpath:config/citizen/rest-client.properties"
})
public class CitizenRestConnectorConfig {

    @Value("${feign.logger.level}")
    private String loggerLevel;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder(objectMapper);
    }

    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder(objectMapper);
    }

//    @Bean
//    public Decoder feignDecoder() {
//        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
//        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
//
//        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
//    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.valueOf(loggerLevel);
    }

}
