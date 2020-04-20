package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.rest.model.RestConnectorRequest;
import eu.sia.meda.connector.rest.transformer.IRestRequestTransformer;
import eu.sia.meda.connector.rest.transformer.request.BaseSimpleRestRequestTransformer;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
class CitizenUpdateRequestTransformer
        extends BaseSimpleRestRequestTransformer
        implements IRestRequestTransformer<CitizenDto, CitizenDto> {

    @Override
    public RestConnectorRequest<CitizenDto> transform(CitizenDto om, Object... args) {
        RestConnectorRequest<CitizenDto> out = new RestConnectorRequest<>();
        out.setMethod(HttpMethod.PUT);
        out.setRequest(om);
        readArgs(out, args);

        return out;
    }

}
