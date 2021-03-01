package it.gov.pagopa.bpd.enrollment.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class PaymentInstrumenWarnException extends MedaDomainRuntimeException {

    private static final String CODE = "payment intrument exception";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;


    public <K extends Serializable> PaymentInstrumenWarnException(String id) {
        super(getMessage(id), CODE, STATUS);
    }

    private static String getMessage(String id) {
        return String.format("payment intrument exception", id);
    }

}
