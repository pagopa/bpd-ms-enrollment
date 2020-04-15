package it.gov.pagopa.bpd.enrollment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.DummyConfiguration;
import it.gov.pagopa.bpd.enrollment.command.EnrollPaymentInstrumentCommand;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.factory.ModelFactory;
import it.gov.pagopa.bpd.enrollment.factory.PaymentInstrumentFactory;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {BpdEnrollmentControllerImpl.class}, excludeAutoConfiguration = MockMvcSecurityAutoConfiguration.class)
@ContextConfiguration(classes = {BpdEnrollmentControllerImpl.class, DummyConfiguration.class, PaymentInstrumentFactory.class})
public class BpdEnrollmentControllerImplTest {

    private static final String URL_TEMPLATE_PREFIX = "/bpd/enrollment";
    private static final OffsetDateTime CURRENT_OFFSET_DATE_TIME = OffsetDateTime.now(ZoneOffset.UTC);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnrollPaymentInstrumentCommand paymentInstrumentCommandMock;

    @SpyBean
    private ModelFactory<EnrollmentDto, PaymentInstrumentDto> paymentInstrumentFactoryMock;


    @Test
    public void enrollPaymentInstrumentIO_OK() throws Exception {
        final String hashPanValue = "hashPan";
        final String fiscalCodeValue = "test";
        EnrollmentDto request = new EnrollmentDto();
        request.setActivationDate(CURRENT_OFFSET_DATE_TIME);

        reset(paymentInstrumentCommandMock);
        when(paymentInstrumentCommandMock.execute())
                .thenAnswer(invocation -> {
                    PaymentInstrumentResource result = new PaymentInstrumentResource();
                    result.setStatus(PaymentInstrumentResource.Status.ACTIVE);
                    result.setActivationDate(request.getActivationDate());
                    result.setHpan(hashPanValue);
                    result.setFiscalCode(fiscalCodeValue);

                    return result;
                });

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/io/payment-instruments/" + hashPanValue)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        PaymentInstrumentResource result =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PaymentInstrumentResource.class);

        verify(paymentInstrumentFactoryMock, only()).createModel(eq(request));
        verify(paymentInstrumentFactoryMock, times(1)).createModel(eq(request));

        assertNotNull(result);
        assertEquals(hashPanValue, result.getHpan());
        assertEquals(fiscalCodeValue, result.getFiscalCode());
        assertEquals(request.getActivationDate(), result.getActivationDate());
        assertEquals(PaymentInstrumentResource.Status.ACTIVE, result.getStatus());
    }


    @Test
    public void enrollPaymentInstrumentIO_OKBadRequest() throws Exception {
        final String hashPanValue = "hashPan";
        final String fiscalCodeValue = "test";
        EnrollmentDto request = new EnrollmentDto();
        request.setActivationDate(null);

        reset(paymentInstrumentCommandMock);
        when(paymentInstrumentCommandMock.execute())
                .thenAnswer(invocation -> {
                    PaymentInstrumentResource result = new PaymentInstrumentResource();
                    result.setStatus(PaymentInstrumentResource.Status.ACTIVE);
                    result.setActivationDate(request.getActivationDate());
                    result.setHpan(hashPanValue);
                    result.setFiscalCode(fiscalCodeValue);

                    return result;
                });

        mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/io/payment-instruments/" + hashPanValue)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }


    //    @Test
    public void enrollPaymentInstrumentIO_KOException() throws Exception {
        final String hashPanValue = "hashPan";
        EnrollmentDto request = new EnrollmentDto();
        request.setActivationDate(CURRENT_OFFSET_DATE_TIME);

        reset(paymentInstrumentCommandMock);
        BDDMockito.willThrow(new RuntimeException())
                .given(paymentInstrumentCommandMock).execute();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/io/payment-instruments/" + hashPanValue)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        PaymentInstrumentResource result =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PaymentInstrumentResource.class);

        verify(paymentInstrumentFactoryMock, only()).createModel(eq(request));
        verify(paymentInstrumentFactoryMock, times(1)).createModel(eq(request));
    }

}