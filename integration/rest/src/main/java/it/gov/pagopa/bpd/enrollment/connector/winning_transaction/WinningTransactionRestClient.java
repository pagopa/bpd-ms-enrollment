package it.gov.pagopa.bpd.enrollment.connector.winning_transaction;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

/**
 * Winning Transaction Rest Client
 */
@FeignClient(name = "${rest-client.winning-transaction.serviceCode}", url = "${rest-client.winning-transaction.base-url}")
public interface WinningTransactionRestClient {

    @DeleteMapping(value = "${rest-client.winning-transaction.delete.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    void deleteByFiscalCode(@NotBlank @PathVariable("fiscalCode") String fiscalCode);

    @PutMapping(value = "${rest-client.winning-transaction.rollback.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void rollback(
            @ApiParam(required = true)
            @PathVariable("fiscalCode")
            @NotBlank
                    String fiscalCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    OffsetDateTime requestTimestamp
    );

}



