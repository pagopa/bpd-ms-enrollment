package it.gov.pagopa.bpd.enrollment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.DummyConfiguration;
import eu.sia.meda.error.config.LocalErrorConfig;
import eu.sia.meda.error.handler.MedaExceptionHandler;
import eu.sia.meda.error.service.impl.LocalErrorManagerServiceImpl;
import it.gov.pagopa.bpd.common.factory.ModelFactory;
import it.gov.pagopa.bpd.enrollment.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.enrollment.assembler.PaymentInstrumentResourceAssembler;
import it.gov.pagopa.bpd.enrollment.command.DeleteEnrolledCitizenCommand;
import it.gov.pagopa.bpd.enrollment.command.EnrollPaymentInstrumentCommand;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenDto;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.connector.payment_instrument.model.PaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.exception.CitizenNotEnabledException;
import it.gov.pagopa.bpd.enrollment.factory.PaymentInstrumentFactory;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentPaymentInstrumentResource;
import it.gov.pagopa.bpd.enrollment.service.CitizenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {BpdEnrollmentControllerImpl.class}, excludeAutoConfiguration = MockMvcSecurityAutoConfiguration.class)
@ContextConfiguration(classes = {
        BpdEnrollmentControllerImpl.class,
        DummyConfiguration.class,
        PaymentInstrumentFactory.class,
        MedaExceptionHandler.class,
        LocalErrorManagerServiceImpl.class
})
@Import(LocalErrorConfig.class)
@TestPropertySource(properties = "error-manager.enabled=true")
public class BpdEnrollmentControllerImplTest {

    private static final String URL_TEMPLATE_PREFIX = "/bpd/enrollment";
    private static final OffsetDateTime CURRENT_OFFSET_DATE_TIME = OffsetDateTime.now();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnrollPaymentInstrumentCommand paymentInstrumentCommandMock;

    @SpyBean
    private PaymentInstrumentResourceAssembler paymentInstrumentResourceAssembler;

    @SpyBean
    private CitizenResourceAssembler citizenResourceAssembler;

    @MockBean
    private DeleteEnrolledCitizenCommand deleteEnrolledCitizenCommandMock;

    @MockBean
    private CitizenService citizenService;

    @SpyBean
    private ModelFactory<EnrollmentPaymentInstrumentDto, PaymentInstrumentDto> paymentInstrumentFactoryMock;


    @Test
    public void enrollPaymentInstrumentIO_OK() throws Exception {
        final String hashPan = "hashPan";
        final String fiscalCode = "DHFIVD85M84D048L";
        EnrollmentPaymentInstrumentDto request = new EnrollmentPaymentInstrumentDto();
        request.setFiscalCode(fiscalCode);

        when(paymentInstrumentCommandMock.execute())
                .thenAnswer(invocation -> {
                    PaymentInstrumentResource result = new PaymentInstrumentResource();
                    result.setActivationDate(CURRENT_OFFSET_DATE_TIME);

                    return result;
                });

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/io/payment-instruments/" + hashPan)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        EnrollmentPaymentInstrumentResource result =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EnrollmentPaymentInstrumentResource.class);

        verify(paymentInstrumentFactoryMock, only()).apply(eq(request));
        verify(paymentInstrumentFactoryMock, times(1)).apply(eq(request));
        verify(paymentInstrumentCommandMock, only()).execute();
        verify(paymentInstrumentCommandMock, times(1)).execute();
        BDDMockito.verify(paymentInstrumentResourceAssembler).toResource(Mockito.any(PaymentInstrumentResource.class));

        assertNotNull(result);
        assertEquals(CURRENT_OFFSET_DATE_TIME, result.getActivationDate());
    }


    @Test
    public void enrollPaymentInstrumentIO_BadRequest() throws Exception {
        final String hashPanValue = "hashPan";
        final String fiscalCode = "DHFIVD85M84D048L";
        EnrollmentPaymentInstrumentDto request = new EnrollmentPaymentInstrumentDto();
        request.setFiscalCode(null);

        mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/io/payment-instruments/" + hashPanValue)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }


    @Test
    public void enrollPaymentInstrumentIO_KOException() throws Exception {
        final String hashPan = "hashPan";
        final String fiscalCode = "DHFIVD85M84D048L";
        EnrollmentPaymentInstrumentDto request = new EnrollmentPaymentInstrumentDto();
        request.setFiscalCode(fiscalCode);

        BDDMockito.willThrow(new CitizenNotEnabledException("pippo"))
                .given(paymentInstrumentCommandMock).execute();

        mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/io/payment-instruments/" + hashPan)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        verify(paymentInstrumentFactoryMock, only()).apply(eq(request));
        verify(paymentInstrumentFactoryMock, times(1)).apply(eq(request));
    }


    @Test
    public void enrollPaymentInstrumentHB_OK() throws Exception {
        final String hashPan = "hashPan";
        final String fiscalCode = "DHFIVD85M84D048L";
        EnrollmentPaymentInstrumentDto request = new EnrollmentPaymentInstrumentDto();
        request.setFiscalCode(fiscalCode);

        when(paymentInstrumentCommandMock.execute())
                .thenAnswer(invocation -> {
                    PaymentInstrumentResource result = new PaymentInstrumentResource();
                    result.setActivationDate(CURRENT_OFFSET_DATE_TIME);

                    return result;
                });

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/hb/payment-instruments/" + hashPan)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        EnrollmentPaymentInstrumentResource result =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EnrollmentPaymentInstrumentResource.class);

        verify(paymentInstrumentFactoryMock, only()).apply(eq(request));
        verify(paymentInstrumentFactoryMock, times(1)).apply(eq(request));
        verify(paymentInstrumentCommandMock, only()).execute();
        verify(paymentInstrumentCommandMock, times(1)).execute();
        BDDMockito.verify(paymentInstrumentResourceAssembler).toResource(Mockito.any(PaymentInstrumentResource.class));


        assertNotNull(result);
        assertEquals(CURRENT_OFFSET_DATE_TIME, result.getActivationDate());
    }


    @Test
    public void enrollPaymentInstrumentHB_BadRequest() throws Exception {
        final String hashPan = "hashPan";
        EnrollmentPaymentInstrumentDto request = new EnrollmentPaymentInstrumentDto();

        mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/hb/payment-instruments/" + hashPan)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }


    @Test
    public void enrollCitizenIO_OK() throws Exception {
        final String fiscalCode = "DHFIVD85M84D048L";

        when(citizenService.enroll(anyString()))
                .thenAnswer(invocation -> {
                    CitizenResource result = new CitizenResource();
                    result.setFiscalCode(invocation.getArgument(0));
                    return result;
                });

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/io/citizens/" + fiscalCode)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CitizenResource result =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CitizenResource.class);

        verify(citizenService, only()).enroll(eq(fiscalCode));
        verify(citizenService, times(1)).enroll(eq(fiscalCode));
        BDDMockito.verify(citizenResourceAssembler).toResource(Mockito.any(CitizenResource.class));
        assertNotNull(result);
        assertEquals(fiscalCode, result.getFiscalCode());
    }


    @Test
    public void enrollCitizenIO_BadRequest() throws Exception {
        final String fiscalCode = "";

        mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/io/citizens/" + fiscalCode)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        verifyZeroInteractions(citizenService);
    }


    @Test
    public void enrollCitizenHB_OK() throws Exception {
        final String fiscalCode = "DHFIVD85M84D048L";

        when(citizenService.enroll(anyString()))
                .thenAnswer(invocation -> {
                    CitizenResource result = new CitizenResource();
                    result.setFiscalCode(invocation.getArgument(0));
                    return result;
                });

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/hb/citizens/" + fiscalCode)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CitizenResource result =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CitizenResource.class);

        verify(citizenService, only()).enroll(eq(fiscalCode));
        verify(citizenService, times(1)).enroll(eq(fiscalCode));
        BDDMockito.verify(citizenResourceAssembler).toResource(Mockito.any(CitizenResource.class));

        assertNotNull(result);
        assertEquals(fiscalCode, result.getFiscalCode());
    }


    @Test
    public void enrollCitizenHB_BadRequest() throws Exception {
        final String fiscalCode = "";
        CitizenDto request = new CitizenDto();
        request.setTimestampTC(CURRENT_OFFSET_DATE_TIME);

        mvc.perform(MockMvcRequestBuilders
                .put(URL_TEMPLATE_PREFIX + "/hb/citizens/" + fiscalCode)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        verifyZeroInteractions(citizenService);
    }

    @Test
    public void deleteByFiscalCode_OK() throws Exception {
        final String fiscalCode = "DHFIVD85M84D048L";
        final String channel = "channel";
        CitizenDto request = new CitizenDto();
        request.setTimestampTC(CURRENT_OFFSET_DATE_TIME);

        BDDMockito.doReturn(true).when(deleteEnrolledCitizenCommandMock).execute();

        mvc.perform(MockMvcRequestBuilders
                .delete("/bpd/citizen/" + fiscalCode + "/" + channel)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        verify(deleteEnrolledCitizenCommandMock, times(1)).execute();

    }

    @Test
    public void deleteByFiscalCode_KO() throws Exception {
        final String fiscalCode = "DHFIVD85M84D048L";
        final String channel = "channel";
        CitizenDto request = new CitizenDto();
        request.setTimestampTC(CURRENT_OFFSET_DATE_TIME);

        BDDMockito.doReturn(false).when(deleteEnrolledCitizenCommandMock).execute();

        mvc.perform(MockMvcRequestBuilders
                .delete("/bpd/citizen/" + fiscalCode + "/" + channel)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andReturn();

        verify(deleteEnrolledCitizenCommandMock, times(1)).execute();

    }


}