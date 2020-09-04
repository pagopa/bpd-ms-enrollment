package it.gov.pagopa.bpd.enrollment.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * @see it.gov.pagopa.bpd.enrollment.service.CitizenService
 */
@Service
class CitizenServiceImpl extends BaseService implements CitizenService {

    private final CitizenRestClient restClient;


    @Autowired
    public CitizenServiceImpl(CitizenRestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public CitizenResource findById(String fiscalCode) {
        return restClient.findById(fiscalCode);
    }


    @Override
    public CitizenResource enroll(String fiscalCode) {
        CitizenDto dto = new CitizenDto();
        dto.setTimestampTC(OffsetDateTime.now());
        return restClient.update(fiscalCode, dto);
    }

    @Override
    public void delete(String fiscalCode) {
        restClient.delete(fiscalCode);
    }
}
