package it.gov.pagopa.bpd.enrollment.connector.citizen.config;

import eu.sia.meda.core.interceptors.BaseContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Map;

public class CopyHeadersInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Map<String, String> headers = BaseContextHolder.getApplicationContext().getCopyHeader();
        if (headers != null) {
            for (Map.Entry<String, String> h : headers.entrySet()) {
                template.header(h.getKey(), h.getValue());
            }
        }
    }

}
