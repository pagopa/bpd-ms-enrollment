package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;

/**
 * Service to manage the Business Logic related to Citizen
 */
public interface CitizenService {

    CitizenResource findById(String fiscalCode);

    CitizenResource update(String fiscalCode, CitizenDto dto);

    CitizenResource enroll(String fiscalCode);

    void delete(String fiscalCode);

}
