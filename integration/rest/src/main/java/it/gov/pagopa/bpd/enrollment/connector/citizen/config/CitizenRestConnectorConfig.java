package it.gov.pagopa.bpd.enrollment.connector.citizen.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.*;
import java.util.stream.Collectors;

//@Slf4j
@Configuration
@EnableFeignClients(basePackages = "it.gov.pagopa.bpd.enrollment.connector.citizen")
@PropertySource({
        "classpath:config/citizen/CitizenFindByIdRestConnector.properties",
        "classpath:config/citizen/CitizenUpdateRestConnector.properties",
        "classpath:config/citizen/rest-client.properties"
})
public class CitizenRestConnectorConfig {

    @Autowired
    private ObjectMapper objectMapper;


//    @Bean
//    public RequestInterceptor copyHeadersInterceptor() {
//        return requestTemplate -> {
//            Map<String, String> headers = BaseContextHolder.getApplicationContext().getCopyHeader();
//            if (headers != null) {
//                for (Map.Entry<String, String> h : headers.entrySet()) {
//                    requestTemplate.header(h.getKey(), h.getValue());
//                }
//            }
//        };
//    }

//    @Bean
//    public RequestInterceptor copyHeadersInterceptor() {
//        return new CopyHeadersInterceptor();
//    }

    //    @Bean
//    public RequestInterceptor addAuthorizationHeaderInterceptor() {
//        return requestTemplate -> {
//            try {
//                String authorizationHeader = BaseContextHolder.getAuthorizationContext().getAuthorizationHeader();
//                if (authorizationHeader != null) {
//                    requestTemplate.header("Authorization", authorizationHeader);
//                }
//            } catch (Exception var3) {
//                if (log.isErrorEnabled()) {
//                    log.error(LoggerUtils.formatArchRow("error retrieving authorization header"));
//                }
//            }
//        };
//    }
    @Bean
    public RequestInterceptor addAuthorizationHeaderInterceptor() {
        return new AddAuthorizationHeaderInterceptor();
    }

    @Bean
    public RequestInterceptor queryParamsPlusEncoderInterceptor() {
        return requestTemplate -> {
            final Map<String, Collection<String>> queriesPlusEncoded = new HashMap<>();
            requestTemplate.queries().forEach((key, value) -> queriesPlusEncoded.put(key, value.stream()
                    .map(paramValue -> paramValue.replace("+", "%2B"))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll)));
            requestTemplate.queries(null);
            requestTemplate.queries(queriesPlusEncoded);
        };
    }


    @Bean
    public Decoder feignDecoder() {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);

        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    @Bean
    public Encoder feignEncoder() {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);

        return new SpringEncoder(objectFactory);
    }

}
