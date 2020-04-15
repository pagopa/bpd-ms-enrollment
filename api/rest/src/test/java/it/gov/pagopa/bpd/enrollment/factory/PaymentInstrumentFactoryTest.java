package it.gov.pagopa.bpd.enrollment.factory;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.PaymentInstrumentDto;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PaymentInstrumentFactoryTest {

    private static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.now();

    private final PaymentInstrumentFactory factory = new PaymentInstrumentFactory();

    @Parameterized.Parameter(0)
    public EnrollmentDto input;

    @Parameterized.Parameter(1)
    public PaymentInstrumentDto expectedOutput;


    @Parameterized.Parameters
    public static Collection<Object[]> getParams() {
        Collection<Object[]> params = new ArrayList<>();
        EnrollmentDto input;
        PaymentInstrumentDto expectedOutput;

        input = new EnrollmentDto();
        input.setActivationDate(OFFSET_DATE_TIME);
        expectedOutput = new PaymentInstrumentDto();
        expectedOutput.setActivationDate(input.getActivationDate());
        expectedOutput.setFiscalCode(null);
        params.add(new Object[]{input, expectedOutput});

        input = new EnrollmentDto();
        input.setActivationDate(null);
        expectedOutput = new PaymentInstrumentDto();
        expectedOutput.setActivationDate(null);
        expectedOutput.setFiscalCode(null);
        params.add(new Object[]{input, expectedOutput});

        input = null;
        expectedOutput = null;
        params.add(new Object[]{input, expectedOutput});

        return params;
    }


    @Test
    public void createModel() {
        try {
            final PaymentInstrumentDto output = factory.createModel(input);

            Assert.assertEquals(expectedOutput, output);

        } catch (RuntimeException e) {
            if (input == null) {
                Assert.assertEquals(IllegalArgumentException.class, e.getClass());
                Assert.assertEquals("Source must not be null", e.getMessage());

            } else {
                throw e;
            }
        }
    }

}