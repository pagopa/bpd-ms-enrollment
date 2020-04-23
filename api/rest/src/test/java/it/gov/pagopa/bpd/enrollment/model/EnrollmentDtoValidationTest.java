package it.gov.pagopa.bpd.enrollment.model;


import it.gov.pagopa.bpd.common.model.validation.FutureOrPresentWithTolerance;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.time.Duration;
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
        enrollmentDto.setActivationDate(OffsetDateTime.now());
        Set<ConstraintViolation<EnrollmentPaymentInstrumentDto>> violations = validator.validate(enrollmentDto);

        Assert.assertTrue(violations.isEmpty());
    }


    @Test
    public void testInvalidActivationDate_notNull() {
        EnrollmentPaymentInstrumentDto enrollmentDto = new EnrollmentPaymentInstrumentDto();
        enrollmentDto.setActivationDate(null);
        Set<ConstraintViolation<EnrollmentPaymentInstrumentDto>> violations = validator.validate(enrollmentDto);

        Assert.assertFalse(violations.isEmpty());
        violations.removeIf(violation ->
                "activationDate".equals(violation.getPropertyPath().toString()) &&
                        NotNull.class.equals(violation.getConstraintDescriptor().getAnnotation().annotationType()));
        Assert.assertTrue(violations.isEmpty());
    }


    @Test
    public void testInvalidActivationDate_FutureOrPresentWithTolerance() {
        EnrollmentPaymentInstrumentDto enrollmentDto = new EnrollmentPaymentInstrumentDto();
        enrollmentDto.setActivationDate(OffsetDateTime.now().minus(Duration.ofMinutes(61)));
        Set<ConstraintViolation<EnrollmentPaymentInstrumentDto>> violations = validator.validate(enrollmentDto);

        Assert.assertFalse(violations.isEmpty());
        violations.removeIf(violation ->
                "activationDate".equals(violation.getPropertyPath().toString()) &&
                        FutureOrPresentWithTolerance.class.equals(violation.getConstraintDescriptor().getAnnotation().annotationType()));
        Assert.assertTrue(violations.isEmpty());
    }
}