package it.gov.pagopa.bpd.enrollment.connector.citizen;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Citizen Rest Client
 */
@FeignClient(name = "${rest-client.citizen.serviceCode}", url = "${rest-client.citizen.base-url}")
public interface CitizenRestClient {

    @GetMapping(value = "${rest-client.citizen.findById.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    CitizenResource findById(@PathVariable("id") String fiscalCode);

    @PutMapping(value = "${rest-client.citizen.update.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    CitizenResource update(@NotBlank @PathVariable("id") String fiscalCode, @RequestBody @Valid CitizenDto dto);

    @DeleteMapping(value = "${rest-client.citizen.delete.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    void delete(@NotBlank @PathVariable("id") String fiscalCode);
}
