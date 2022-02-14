package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;


/**
 * Service to manage the Business Logic related to Citizen
 */
public interface CitizenService {

    CitizenResource findById(String fiscalCode);

    CitizenResource enroll(String fiscalCode, CitizenDto.OptInStatus status);

    void delete(String fiscalCode);

}
