package it.gov.pagopa.bpd.enrollment.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.citizen.PaymentInstrumentRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class PaymentInstrumentServiceImpl extends BaseService implements PaymentInstrumentService {

    private final PaymentInstrumentRestClient restClient;


    @Autowired
    public PaymentInstrumentServiceImpl(PaymentInstrumentRestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public PaymentInstrumentResource update(String hpan, PaymentInstrumentDto paymentInstrument) {
        return restClient.update(hpan, paymentInstrument);
    }

}
