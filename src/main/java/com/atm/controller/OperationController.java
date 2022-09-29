package com.atm.controller;

import com.atm.annotation.validation.MultipleOf10;
import com.atm.exception.NotEnoughCashException;
import com.atm.service.OperationService;
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

  @GetMapping("/check-balance")
  public ResponseEntity<Map<String, Object>> checkBalance() {
    return operationService.checkBalance();
  }

  @PostMapping("/withdrawal/{withdrawal}")
  public ResponseEntity<Map<String, Object>> doWithdrawal(
      @PathVariable("withdrawal")
      @MultipleOf10
      Double withdrawal)
      throws NotEnoughCashException {
    return operationService.doWithdrawal(withdrawal);
  }
}
