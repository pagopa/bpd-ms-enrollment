package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import io.swagger.annotations.ApiParam;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.ChannelValidationResource;
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

    @GetMapping(value = "${rest-client.payment-instrument.find.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    PaymentInstrumentResource find(@NotBlank @PathVariable("hashPan") String hpan);

    @PutMapping(value = "${rest-client.payment-instrument.update.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    PaymentInstrumentResource update(@RequestBody @Valid PaymentInstrumentDto paymentInstrument,
                                     @NotBlank @PathVariable("hashPan") String hpan);

    @PostMapping(value = "${rest-client.payment-instrument.validate.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ChannelValidationResource validateByFiscalCode(@NotBlank @PathVariable("fiscalCode") String fiscalCode,
                                                 @NotBlank @PathVariable("channel") String channel);


}



