package it.gov.pagopa.bpd.enrollment.connector.payment_instrument;

import eu.sia.meda.connector.rest.model.RestConnectorRequest;
import eu.sia.meda.connector.rest.transformer.IRestRequestTransformer;
import eu.sia.meda.connector.rest.transformer.request.BaseSimpleRestRequestTransformer;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
class PaymentInstrumentUpdateRequestTransformer
        extends BaseSimpleRestRequestTransformer
        implements IRestRequestTransformer<PaymentInstrumentDto, PaymentInstrumentDto> {

    @Override
    public RestConnectorRequest<PaymentInstrumentDto> transform(PaymentInstrumentDto om, Object... args) {
        RestConnectorRequest<PaymentInstrumentDto> out = new RestConnectorRequest<>();
        out.setMethod(HttpMethod.PUT);
        out.setRequest(om);
        readArgs(out, args);

        return out;
    }

}
