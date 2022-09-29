package com.zinkworks.atm.validator;

import com.zinkworks.atm.annotation.validation.MultipleOf10;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WithdrawalValidator implements ConstraintValidator<MultipleOf10, Double> {

  private final static Integer MULTIPLE = 10;

  @Override
  public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
    return aDouble % MULTIPLE == 0 && aDouble != 0;
  }
}
