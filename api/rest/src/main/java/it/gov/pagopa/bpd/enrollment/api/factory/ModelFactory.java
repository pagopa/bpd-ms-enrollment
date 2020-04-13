package it.gov.pagopa.bpd.enrollment.api.factory;

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
