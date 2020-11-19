package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import io.swagger.annotations.ApiParam;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

/**
 * Payment Instrument Rest Client
 */
@FeignClient(name = "${rest-client.payment-instrument.serviceCode}", url = "${rest-client.payment-instrument.base-url}")
public interface PaymentInstrumentRestClient {

    @GetMapping(value = "${rest-client.payment-instrument.find.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    PaymentInstrumentResource find(@NotBlank @PathVariable("hashPan") String hpan);

    @PutMapping(value = "${rest-client.payment-instrument.update.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    PaymentInstrumentResource update(@RequestBody @Valid PaymentInstrumentDto paymentInstrument,
                                     @NotBlank @PathVariable("hashPan") String hpan);

    @DeleteMapping(value = "${rest-client.payment-instrument.delete.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    void deleteByFiscalCode(@NotBlank @PathVariable("fiscalCode") String fiscalCode,
                            @NotBlank @PathVariable("channel") String channel);

    @PutMapping(value = "${rest-client.payment-instrument.rollback.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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



