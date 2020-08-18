package it.gov.pagopa.bpd.enrollment.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

public class CitizenNotEnabledException extends MedaDomainRuntimeException {

    public CitizenNotEnabledException(String fiscalCode) {
        super(getMessage("Citizen", fiscalCode), "user.not-enabled.error", HttpStatus.FORBIDDEN);
    }

    private static String getMessage(String resourceClass, Object id) {
        return String.format("T&C NOT ACCEPTED", resourceClass, id);
    }

}
