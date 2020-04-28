package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.rest.transformer.request.SimpleRestGetRequestTransformer;
import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @see it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenRestClient
 */
@Service
class CitizenRestClientImpl extends BaseService implements CitizenRestClient {

    static final String FISCAL_CODE_PARAM_KEY = "fiscalCode";

    private final CitizenFindByIdRestConnector findByIdRestConnector;
    private final SimpleRestGetRequestTransformer getRequestTransformer;
    private final CitizenFindByIdResponseTransformer findByIdResponseTransformer;

    private final CitizenUpdateRestConnector updateRestConnector;
    private final CitizenUpdateRequestTransformer updateRequestTransformer;
    private final CitizenUpdateResponseTransformer updateResponseTransformer;


    @Autowired
    public CitizenRestClientImpl(CitizenFindByIdRestConnector findByIdRestConnector,
                                 SimpleRestGetRequestTransformer getRequestTransformer,
                                 CitizenFindByIdResponseTransformer findByIdResponseTransformer,
                                 CitizenUpdateRestConnector updateRestConnector,
                                 CitizenUpdateRequestTransformer updateRequestTransformer,
                                 CitizenUpdateResponseTransformer updateResponseTransformer) {
        this.findByIdRestConnector = findByIdRestConnector;
        this.getRequestTransformer = getRequestTransformer;
        this.findByIdResponseTransformer = findByIdResponseTransformer;
        this.updateRestConnector = updateRestConnector;
        this.updateRequestTransformer = updateRequestTransformer;
        this.updateResponseTransformer = updateResponseTransformer;
    }


    public CitizenResource findById(String fiscalCode) {
        if (fiscalCode == null) {
            throw new IllegalArgumentException("Fiscal Code cannot be null");
        }

        final HashMap<String, Object> params = new HashMap<>();
        params.put(FISCAL_CODE_PARAM_KEY, fiscalCode);

        return findByIdRestConnector.call(null, getRequestTransformer, findByIdResponseTransformer, params);
    }


    @Override
    public CitizenResource update(String fiscalCode, CitizenDto dto) {
        if (fiscalCode == null) {
            throw new IllegalArgumentException("Fiscal Code cannot be null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Request body cannot be null");
        }

        final HashMap<String, Object> params = new HashMap<>();
        params.put(FISCAL_CODE_PARAM_KEY, fiscalCode);

        return updateRestConnector.call(dto, updateRequestTransformer, updateResponseTransformer, params);
    }

}
