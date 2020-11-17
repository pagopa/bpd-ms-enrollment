package it.gov.pagopa.bpd.enrollment.connector.winning_transaction;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;

/**
 * Winning Transaction Rest Client
 */
@FeignClient(name = "${rest-client.winning-transaction.serviceCode}", url = "${rest-client.winning-transaction.base-url}")
public interface WinningTransactionRestClient {

    @DeleteMapping(value = "${rest-client.winning-transaction.delete.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    void deleteByFiscalCode(@NotBlank @PathVariable("fiscalCode") String fiscalCode);

}



