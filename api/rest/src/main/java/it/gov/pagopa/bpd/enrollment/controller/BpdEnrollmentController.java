package it.gov.pagopa.bpd.enrollment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import it.gov.pagopa.bpd.common.annotation.UpperCase;
import it.gov.pagopa.bpd.common.util.Constants;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali enrollment Controller")
@RequestMapping("/bpd/enrollment")
@Validated
public interface BpdEnrollmentController {

    @PutMapping(value = "io/citizens/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource enrollCitizenIO(
            @ApiParam(value = "${swagger.enrollment.fiscalCode}", required = true)
            @PathVariable("id") @UpperCase
            @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @RequestBody @Valid CitizenDto request);


    @PutMapping(value = "/io/payment-instruments/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    PaymentInstrumentResource enrollPaymentInstrumentIO(
            @ApiParam(value = "${swagger.enrollment.hashPan}", required = true)
            @PathVariable("id")
            @NotBlank
                    String hashPan,
            @RequestBody @Valid EnrollmentPaymentInstrumentDto request) throws Exception;


    @PutMapping(value = "hb/citizens/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource enrollCitizenHB(
            @ApiParam(value = "${swagger.enrollment.fiscalCode}", required = true)
            @PathVariable("id") @UpperCase
            @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @RequestBody @Valid CitizenDto request
    );


    @PutMapping(value = "/hb/payment-instruments/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    PaymentInstrumentResource enrollPaymentInstrumentHB(
            @ApiParam(value = "${swagger.enrollment.hashPan}", required = true)
            @PathVariable("id")
            @NotBlank
                    String hashPan,
            @RequestBody @Valid EnrollmentPaymentInstrumentDto request) throws Exception;

}
