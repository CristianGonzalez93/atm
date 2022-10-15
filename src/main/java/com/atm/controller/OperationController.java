package com.atm.controller;

import com.atm.annotation.validation.MultipleOf5;
import com.atm.exception.NotEnoughCashException;
import com.atm.service.OperationService;
import io.github.gonguasp.jwt.annotation.AccessTokenVerification;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@AllArgsConstructor
public class OperationController {

  @Autowired
  private OperationService operationService;

  @AccessTokenVerification
  @GetMapping("/check-balance")
  public ResponseEntity<Map<String, Object>> checkBalance() {
    return operationService.checkBalance();
  }

  @AccessTokenVerification
  @PostMapping("/withdrawal/{withdrawal}")
  public ResponseEntity<Map<String, Object>> doWithdrawal(
      @PathVariable("withdrawal")
      @MultipleOf5
      Double withdrawal)
      throws NotEnoughCashException {
    return operationService.doWithdrawal(withdrawal);
  }
}
