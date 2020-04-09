package it.gov.pagopa.bpd.enrollment.connector.citizen;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;

public interface CitizenRestClient {

    CitizenResource findById(String fiscalCode);

}
