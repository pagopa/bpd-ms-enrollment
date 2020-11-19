package it.gov.pagopa.bpd.enrollment.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.PaymentInstrumentRestClient;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * @see it.gov.pagopa.bpd.enrollment.service.PaymentInstrumentService
 */
@Service
class PaymentInstrumentServiceImpl extends BaseService implements PaymentInstrumentService {

    private final PaymentInstrumentRestClient restClient;


    @Autowired
    public PaymentInstrumentServiceImpl(PaymentInstrumentRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public PaymentInstrumentResource find(String hpan) {
        return restClient.find(hpan);
    }

    @Override
    public PaymentInstrumentResource update(String hpan, PaymentInstrumentDto paymentInstrument) {
        return restClient.update(paymentInstrument, hpan);
    }

    @Override
    public void deleteByFiscalCode(String fiscalCode, String channel) {
        restClient.deleteByFiscalCode(fiscalCode, channel);
    }

    @Override
    public void rollback(String fiscalCode, OffsetDateTime requestTimestamp) {
        restClient.rollback(fiscalCode, requestTimestamp);
    }

}
