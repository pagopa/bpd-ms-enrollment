package it.gov.pagopa.bpd.enrollment.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenFeignRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @see it.gov.pagopa.bpd.enrollment.service.CitizenService
 */
@Service
class CitizenServiceImpl extends BaseService implements CitizenService {

    private final CitizenRestClient restClient;
    private final CitizenFeignRestClient feignRestClient;


    @Autowired
    public CitizenServiceImpl(CitizenRestClient restClient, CitizenFeignRestClient feignRestClient) {
        this.restClient = restClient;
        this.feignRestClient = feignRestClient;
    }


    @Override
    public CitizenResource findById(String fiscalCode) {
        return feignRestClient.findById(fiscalCode);
    }


    @Override
    public CitizenResource update(String fiscalCode, CitizenDto dto) {
        return restClient.update(fiscalCode, dto);
    }
}
