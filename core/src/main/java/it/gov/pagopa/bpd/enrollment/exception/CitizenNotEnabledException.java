package it.gov.pagopa.bpd.enrollment.exception;

import it.gov.pagopa.bpd.common.exception.ResourceNotEnabledException;

public class CitizenNotEnabledException extends ResourceNotEnabledException {

    public CitizenNotEnabledException(String id) {
        super("Citizen", id);
    }

}
