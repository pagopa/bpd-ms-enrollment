package it.gov.pagopa.bpd.enrollment.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.enrollment.connector.winning_transaction.WinningTransactionRestClient;

public class WinningTransactionServiceImpl extends BaseService implements WinningTransactionService {

    private final WinningTransactionRestClient restClient;

    public WinningTransactionServiceImpl(WinningTransactionRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void deleteByFiscalCode(String fiscalCode) {
        restClient.deleteByFiscalCode(fiscalCode);
    }
}