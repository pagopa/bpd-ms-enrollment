package it.gov.pagopa.bpd.enrollment.model;


import org.junit.Assert;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Set;

public class EnrollmentDtoValidationTest {

    private final Validator validator;


    public EnrollmentDtoValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testValid() {
        EnrollmentPaymentInstrumentDto enrollmentDto = new EnrollmentPaymentInstrumentDto();
        enrollmentDto.setFiscalCode("PRNGEA09R54F205G");
        Set<ConstraintViolation<EnrollmentPaymentInstrumentDto>> violations = validator.validate(enrollmentDto);

        Assert.assertTrue(violations.isEmpty());
    }


    @Test
    public void testInvalidFiscalCode_notNull() {
        EnrollmentPaymentInstrumentDto enrollmentDto = new EnrollmentPaymentInstrumentDto();
        Set<ConstraintViolation<EnrollmentPaymentInstrumentDto>> violations = validator.validate(enrollmentDto);
        Assert.assertFalse(violations.isEmpty());
    }

}