package it.gov.pagopa.bpd.enrollment.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.winning_transaction.WinningTransactionRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * @see it.gov.pagopa.bpd.enrollment.service.WinningTransactionService
 */
@Service
class WinningTransactionServiceImpl extends BaseService implements WinningTransactionService {

    private final WinningTransactionRestClient restClient;

    @Autowired
    public WinningTransactionServiceImpl(WinningTransactionRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void deleteByFiscalCode(String fiscalCode) {
        restClient.deleteByFiscalCode(fiscalCode);
    }

    @Override
    public void rollback(String fiscalCode, OffsetDateTime requestTimestamp) {
        restClient.rollback(fiscalCode, requestTimestamp);
    }
}