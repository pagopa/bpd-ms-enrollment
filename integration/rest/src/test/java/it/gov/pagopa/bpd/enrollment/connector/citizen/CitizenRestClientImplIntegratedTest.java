package it.gov.pagopa.bpd.enrollment.connector.citizen;

import eu.sia.meda.connector.meda.ArchMedaInternalConnectorConfigurationService;
import eu.sia.meda.connector.rest.BaseRestConnectorTest;
import eu.sia.meda.connector.rest.transformer.request.SimpleRestGetRequestTransformer;
import eu.sia.meda.connector.rest.transformer.response.SimpleRest2xxResponseTransformer;
import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(
        locations = {
                "classpath:config/BpdEnrollmentRestConnector.properties"
        },
        properties = {
                "connectors.medaInternalConfigurations.items.CitizenFindByIdRestConnector.mocked=true",
                "connectors.medaInternalConfigurations.items.CitizenFindByIdRestConnector.path=citizen/findById"
        })
@Import({ArchMedaInternalConnectorConfigurationService.class,
        CitizenRestClientImpl.class,
        CitizenFindByIdRestConnector.class})
public class CitizenRestClientImplIntegratedTest extends BaseRestConnectorTest {

    @Autowired
    private CitizenRestClient restClient;

    @SpyBean
    private CitizenFindByIdRestConnector connector;
    @SpyBean
    private SimpleRestGetRequestTransformer requestTransformer;
    @SpyBean
    private SimpleRest2xxResponseTransformer<CitizenResource> responseTransformer;


    //    @Test
    public void findById() {
        final CitizenResource result = restClient.findById("prova");

        Assert.assertNotNull(result);
    }

}