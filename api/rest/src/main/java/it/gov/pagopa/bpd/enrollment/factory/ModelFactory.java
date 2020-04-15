package it.gov.pagopa.bpd.enrollment.factory;

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
