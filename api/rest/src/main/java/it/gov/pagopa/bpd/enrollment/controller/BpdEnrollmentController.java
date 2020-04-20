package it.gov.pagopa.bpd.enrollment.controller;

import io.swagger.annotations.Api;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali enrollment Controller")
@RequestMapping("/bpd/enrollment")
public interface BpdEnrollmentController {

    @PutMapping(value = "io/citizen", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource enrollCitizenIO(@RequestBody @Valid CitizenDto request);


    @PutMapping(value = "/io/payment-instruments/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    PaymentInstrumentResource enrollPaymentInstrumentIO(@PathVariable("id") @NotBlank String hashPan,
                                                        @RequestBody @Valid EnrollmentPaymentInstrumentDto request);


    @PutMapping(value = "hb/citizens/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource enrollCitizenHB(@PathVariable("id") @NotBlank String fiscalCode,
                                    @RequestBody @Valid CitizenDto request);


    @PutMapping(value = "/hb/payment-instruments/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    PaymentInstrumentResource enrollPaymentInstrumentHB(@PathVariable("id") @NotBlank String hashPan,
                                                        @RequestBody @Valid PaymentInstrumentDto request);

}
