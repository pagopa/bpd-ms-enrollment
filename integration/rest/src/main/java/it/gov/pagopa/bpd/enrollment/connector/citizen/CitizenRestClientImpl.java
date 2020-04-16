package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.rest.transformer.request.SimpleRestGetRequestTransformer;
import eu.sia.meda.connector.rest.transformer.response.SimpleRest2xxResponseTransformer;
import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
class CitizenRestClientImpl extends BaseService implements CitizenRestClient {

    static final String FISCAL_CODE_PARAM_KEY = "fiscalCode";

    private final CitizenFindByIdRestConnector connector;
    private final SimpleRestGetRequestTransformer requestTransformer;
    private final SimpleRest2xxResponseTransformer<CitizenResource> responseTransformer;


    @Autowired
    public CitizenRestClientImpl(CitizenFindByIdRestConnector connector,
                                 SimpleRestGetRequestTransformer requestTransformer,
                                 SimpleRest2xxResponseTransformer<CitizenResource> responseTransformer) {
        this.connector = connector;
        this.requestTransformer = requestTransformer;
        this.responseTransformer = responseTransformer;
    }


    public CitizenResource findById(String fiscalCode) {
        if (fiscalCode == null) {
            throw new IllegalArgumentException("Fiscal Code cannot be null");
        }

        final HashMap<String, Object> params = new HashMap<>();
        params.put(FISCAL_CODE_PARAM_KEY, fiscalCode);
        return connector.call(null, requestTransformer, responseTransformer, params);
    }

}
