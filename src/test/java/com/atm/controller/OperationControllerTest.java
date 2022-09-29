package com.atm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.atm.service.OperationService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OperationControllerTest {

  private static OperationService operationService;
  private static OperationController operationController;

  @BeforeAll
  private static void beforeAll() {
    operationService = Mockito.mock(OperationService.class);
    operationController = new OperationController(operationService);
  }

  @Test
  void checkBalanceTest() {
    Mockito.when(operationService.checkBalance()).thenReturn(ResponseEntity.ok(null));
    assertEquals(HttpStatus.OK, operationController.checkBalance().getStatusCode());
  }

  @Test
  @SneakyThrows
  void doWithdrawalTest() {
    Mockito.when(operationService.doWithdrawal(Mockito.anyDouble())).thenReturn(ResponseEntity.ok(null));
    assertEquals(HttpStatus.OK, operationController.doWithdrawal(0.0).getStatusCode());
  }
}
