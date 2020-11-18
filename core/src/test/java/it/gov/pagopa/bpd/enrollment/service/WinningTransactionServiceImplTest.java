package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.winning_transaction.WinningTransactionRestClient;
import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = WinningTransactionServiceImpl.class)
public class WinningTransactionServiceImplTest {

    @MockBean
    private WinningTransactionRestClient restClientMock;

    @Autowired
    private WinningTransactionServiceImpl winningTransactionService;

    @Test
    public void testDeleteByFiscalCode() {
        String fiscalCode = RandomStringUtils.randomAlphanumeric(16);

        winningTransactionService.deleteByFiscalCode(fiscalCode);

        verify(restClientMock, only()).deleteByFiscalCode(eq(fiscalCode));
        verify(restClientMock, times(1)).deleteByFiscalCode(eq(fiscalCode));
    }

    @Test
    public void rollback() {
        String fiscalCode = "testFiscalCode";
        OffsetDateTime requestTimestamp = OffsetDateTime.now();
        winningTransactionService.rollback(fiscalCode, requestTimestamp);

        verify(restClientMock, only()).rollback(eq(fiscalCode), eq(requestTimestamp));
        verify(restClientMock, times(1)).rollback(eq(fiscalCode), eq(requestTimestamp));
    }
}