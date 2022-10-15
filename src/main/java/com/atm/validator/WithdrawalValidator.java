package com.atm.validator;

import com.atm.annotation.validation.MultipleOf5;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WithdrawalValidator implements ConstraintValidator<MultipleOf5, Double> {

  private final static Integer MULTIPLE = 5;

  @Override
  public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
    return aDouble % MULTIPLE == 0 && aDouble != 0;
  }
}
