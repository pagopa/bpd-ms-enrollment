package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.rest.transformer.response.SimpleRest2xxResponseTransformer;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.stereotype.Service;


@Service
class CitizenFindByIdResponseTransformer
        extends SimpleRest2xxResponseTransformer<CitizenResource> {
}
