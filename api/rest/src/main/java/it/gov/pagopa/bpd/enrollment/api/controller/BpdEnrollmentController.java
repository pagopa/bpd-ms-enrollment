package it.gov.pagopa.bpd.enrollment.api.controller;

import io.swagger.annotations.Api;
import it.gov.pagopa.bpd.enrollment.api.model.EnrollmentDTO;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali enrollment Controller")
@RequestMapping("/bpd/enrollment")
public interface BpdEnrollmentController {

    @PostMapping(value = "/io/payment-instruments/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    PaymentInstrumentResource enrollPaymentInstrumentIO(@PathVariable("id") String hashPan, @RequestBody EnrollmentDTO request);

}
