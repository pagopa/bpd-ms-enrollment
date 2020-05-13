package it.gov.pagopa.bpd.enrollment.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenFeignRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @see it.gov.pagopa.bpd.enrollment.service.CitizenService
 */
@Service
class CitizenServiceImpl extends BaseService implements CitizenService {

    private final CitizenFeignRestClient restClient;


    @Autowired
    public CitizenServiceImpl(CitizenFeignRestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public CitizenResource findById(String fiscalCode) {
        return restClient.findById(fiscalCode);
    }


    @Override
    public CitizenResource update(String fiscalCode, CitizenDto dto) {
        return restClient.update(fiscalCode, dto);
    }
}
