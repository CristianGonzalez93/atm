package com.zinkworks.atm.annotation.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.zinkworks.atm.validator.WithdrawalValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = WithdrawalValidator.class)
@Documented
public @interface MultipleOf10 {

  String message() default "{withdrawal.invalid}";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

}
