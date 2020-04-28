package it.gov.pagopa.bpd.enrollment.connector.citizen;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;

/**
 * Citizen Rest Client
 */
public interface CitizenRestClient {

    CitizenResource findById(String fiscalCode);

    CitizenResource update(String fiscalCode, CitizenDto dto);

}
