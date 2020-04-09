package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;

public interface CitizenService {

    CitizenResource findById(String fiscalCode);

}
