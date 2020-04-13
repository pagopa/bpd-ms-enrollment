package it.gov.pagopa.bpd.enrollment.exception;

public class ResourceNotEnabledException extends RuntimeException {

    public ResourceNotEnabledException(String resourceName) {
        super(String.format("Resource %s not enabled", resourceName));
    }

}
