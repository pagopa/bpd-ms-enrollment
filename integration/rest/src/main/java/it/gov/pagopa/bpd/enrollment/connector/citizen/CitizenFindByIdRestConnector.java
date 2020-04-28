package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.meda.MedaInternalConnector;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.stereotype.Service;

/**
 * Rest Connector for Citizen findById service
 */
@Service
class CitizenFindByIdRestConnector
        extends MedaInternalConnector<Void, CitizenResource, Void, CitizenResource> {
}
