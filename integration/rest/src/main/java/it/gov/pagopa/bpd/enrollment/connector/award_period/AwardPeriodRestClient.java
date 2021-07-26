package it.gov.pagopa.bpd.enrollment.connector.award_period;

import it.gov.pagopa.bpd.enrollment.connector.award_period.model.AwardPeriodResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Award Period Rest Client
 */
@FeignClient(name = "${rest-client.award-period.serviceCode}", url = "${rest-client.award-period.base-url}")
public interface AwardPeriodRestClient {

    @GetMapping(value = "${rest-client.award-period.findActives.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<AwardPeriodResource> findActives();

}
