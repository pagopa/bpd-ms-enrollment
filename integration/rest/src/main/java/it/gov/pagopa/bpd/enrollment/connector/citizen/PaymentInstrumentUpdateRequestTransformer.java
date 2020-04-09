package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.rest.model.RestConnectorRequest;
import eu.sia.meda.connector.rest.transformer.IRestRequestTransformer;
import eu.sia.meda.connector.rest.transformer.request.BaseSimpleRestRequestTransformer;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDTO;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
class PaymentInstrumentUpdateRequestTransformer
        extends BaseSimpleRestRequestTransformer
        implements IRestRequestTransformer<PaymentInstrumentDTO, PaymentInstrumentDTO> {

    @Override
    public RestConnectorRequest<PaymentInstrumentDTO> transform(PaymentInstrumentDTO om, Object... args) {
        RestConnectorRequest<PaymentInstrumentDTO> out = new RestConnectorRequest<>();
        out.setMethod(HttpMethod.PUT);
        out.setRequest(om);
        readArgs(out, args);

        return out;
    }

}
