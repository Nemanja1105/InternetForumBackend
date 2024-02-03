package org.unibl.etf.models.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = SqlValidator.class)
@Documented
public @interface SqlValidators {

    String message() default "{Sql injection attack potential}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}