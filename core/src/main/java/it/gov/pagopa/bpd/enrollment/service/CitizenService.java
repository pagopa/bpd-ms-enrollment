package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;

/**
 * Service to manage the Business Logic related to Citizen
 */
public interface CitizenService {

    CitizenResource findById(String fiscalCode);

    CitizenResource enroll(String fiscalCode);

    void delete(String fiscalCode);

}
