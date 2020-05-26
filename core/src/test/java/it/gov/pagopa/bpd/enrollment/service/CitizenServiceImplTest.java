package it.gov.pagopa.bpd.enrollment.service;

import it.gov.pagopa.bpd.enrollment.connector.citizen.CitizenRestClient;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CitizenServiceImpl.class)
public class CitizenServiceImplTest {

    @MockBean
    private CitizenRestClient restClientMock;

    @Autowired
    private CitizenServiceImpl citizenService;


    @PostConstruct
    public void configureTests() {
        when(restClientMock.findById(any(String.class)))
                .thenAnswer(invocation -> {
                    final CitizenResource result = new CitizenResource();
                    result.setFiscalCode(invocation.getArgument(0));
                    result.setTimestampTC(OffsetDateTime.now());

                    return result;
                });

        when(restClientMock.update(anyString(), any(CitizenDto.class)))
                .thenAnswer(invocation -> {
                    final CitizenResource result = new CitizenResource();
                    result.setFiscalCode(invocation.getArgument(0));
                    result.setTimestampTC(invocation.getArgument(1, CitizenDto.class).getTimestampTC());

                    return result;
                });
    }


    @Test
    public void findById() {
        String fiscalCode = RandomStringUtils.randomAlphanumeric(16);

        final CitizenResource found = citizenService.findById(fiscalCode);

        verify(restClientMock, only()).findById(eq(fiscalCode));
        verify(restClientMock, times(1)).findById(eq(fiscalCode));
        assertEquals(fiscalCode, found.getFiscalCode());
        assertNotNull(found.getTimestampTC());
    }


    @Test
    public void update() {
        String fiscalCode = RandomStringUtils.randomAlphanumeric(16);
        CitizenDto dto = new CitizenDto();
        dto.setTimestampTC(OffsetDateTime.now());

        final CitizenResource result = citizenService.update(fiscalCode, dto);

        verify(restClientMock, only()).update(eq(fiscalCode), eq(dto));
        verify(restClientMock, times(1)).update(eq(fiscalCode), eq(dto));
        assertEquals(fiscalCode, result.getFiscalCode());
        assertEquals(dto.getTimestampTC(), result.getTimestampTC());
    }

}