package it.gov.pagopa.bpd.enrollment.connector.citizen.config;

import eu.sia.meda.config.LoggerUtils;
import eu.sia.meda.core.interceptors.BaseContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddAuthorizationHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        try {
            String authorizationHeader = BaseContextHolder.getAuthorizationContext().getAuthorizationHeader();
            if (authorizationHeader != null) {
                template.header("Authorization", authorizationHeader);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(LoggerUtils.formatArchRow("error retrieving authorization header"));
            }
        }
    }

}
