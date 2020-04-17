package it.gov.pagopa.bpd.enrollment.controller;

import io.swagger.annotations.Api;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali enrollment Controller")
@RequestMapping("/bpd/enrollment")
public interface BpdEnrollmentController {

    @PutMapping(value = "/io/payment-instruments/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    PaymentInstrumentResource enrollPaymentInstrumentIO(@PathVariable("id") String hashPan, @Valid @RequestBody EnrollmentDto request);

}
