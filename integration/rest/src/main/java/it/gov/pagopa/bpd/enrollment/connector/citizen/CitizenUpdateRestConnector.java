package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.meda.MedaInternalConnector;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.stereotype.Service;

/**
 * Rest Connector for Citizen update service
 */
@Service
class CitizenUpdateRestConnector
        extends MedaInternalConnector<CitizenDto, CitizenResource, CitizenDto, CitizenResource> {
}
