package it.gov.pagopa.bpd.enrollment.service;

import java.time.OffsetDateTime;

/**
 * Service to manage the Business Logic related to Winning Transaction
 */
public interface WinningTransactionService {

    void deleteByFiscalCode(String fiscalCode);

    void rollback(String fiscalCode, OffsetDateTime requestTimestamp);

}
