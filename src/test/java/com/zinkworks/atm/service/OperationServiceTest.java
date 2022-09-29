package com.zinkworks.atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zinkworks.atm.config.SecurityConfiguration;
import com.zinkworks.atm.dto.UserDetailsDTO;
import com.zinkworks.atm.exception.NotEnoughBillsException;
import com.zinkworks.atm.exception.NotEnoughFundsException;
import com.zinkworks.atm.exception.NotEnoughMoneyException;
import com.zinkworks.atm.repository.AccountRepository;
import com.zinkworks.atm.repository.BillRepository;
import com.zinkworks.atm.repository.domain.Account;
import com.zinkworks.atm.repository.domain.Bill;
import com.zinkworks.atm.util.JwtUtils;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OperationServiceTest {

  private static HttpServletRequest request;
  private static BillRepository billRepository;
  private static AccountRepository accountRepository;
  private static JwtUtils jwtUtils;
  private static OperationService operationService;

  @BeforeAll
  private static void beforeAll() {
    request = Mockito.mock(HttpServletRequest.class);
    billRepository = Mockito.mock(BillRepository.class);
    accountRepository = Mockito.mock(AccountRepository.class);
    jwtUtils = Mockito.mock(JwtUtils.class);
    operationService = new OperationService(request, billRepository, accountRepository, jwtUtils);
  }

  @Test
  void checkBalanceTest() {
    Mockito.when(request.getHeader(SecurityConfiguration.ACCESS_TOKEN)).thenReturn("test");
    Mockito.when(jwtUtils.validateToken(Mockito.anyString())).thenReturn(
        new Account(0, "0", "0", 100.0, 0.0));
    Mockito.when(billRepository.findAll()).thenReturn(List.of(new Bill(0, 50.0, 2)));
    ResponseEntity<Map<String, Object>> response = operationService.checkBalance();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().containsKey("balance"));
    assertTrue(response.getBody().containsKey("max withdrawal"));
  }

  @Test
  @SneakyThrows
  void doWithdrawalTest() {
    Mockito.when(request.getHeader(SecurityConfiguration.ACCESS_TOKEN)).thenReturn("test");
    Mockito.when(jwtUtils.validateToken(Mockito.anyString())).thenReturn(
        new Account(0, "0", "0", 100.0, 0.0));
    Mockito.when(billRepository.findAll()).thenReturn(List.of(new Bill(0, 50.0, 2)));
    Mockito.when(billRepository.findAllByOrderByBillValueDesc()).thenReturn(List.of(
        new Bill(0, 50.0, 2)));
    ResponseEntity<Map<String, Object>> response = operationService.doWithdrawal(50.0);;
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(!((List<Bill>) response.getBody().get("notes")).isEmpty());
    assertEquals(50.0, ((Double) response.getBody().get("withdrawal")));
    assertEquals(50.0, ((Double) response.getBody().get("remaining balance")));
  }

  @Test
  @SneakyThrows
  void doWithdrawalTestNotEnoughFundsException() {
    Mockito.when(request.getHeader(SecurityConfiguration.ACCESS_TOKEN)).thenReturn("test");
    Mockito.when(jwtUtils.validateToken(Mockito.anyString())).thenReturn(
        new Account(0, "0", "0", 100.0, 0.0));
    Mockito.when(billRepository.findAll()).thenReturn(List.of(new Bill(0, 50.0, 2)));
    Mockito.when(billRepository.findAllByOrderByBillValueDesc()).thenReturn(List.of(
        new Bill(0, 50.0, 2)));
    assertThrows(NotEnoughFundsException.class, () ->
      operationService.doWithdrawal(200.0)
    );
  }

  @Test
  @SneakyThrows
  void doWithdrawalTestNotEnoughBillsException() {
    Mockito.when(request.getHeader(SecurityConfiguration.ACCESS_TOKEN)).thenReturn("test");
    Mockito.when(jwtUtils.validateToken(Mockito.anyString())).thenReturn(
        new Account(0, "0", "0", 100.0, 0.0));
    Mockito.when(billRepository.findAll()).thenReturn(List.of(new Bill(0, 50.0, 2)));
    Mockito.when(billRepository.findAllByOrderByBillValueDesc()).thenReturn(List.of(
        new Bill(0, 50.0, 2)));
    assertThrows(NotEnoughBillsException.class, () ->
      operationService.doWithdrawal(80.0)
    );
  }

  @Test
  @SneakyThrows
  void doWithdrawalTestNotEnoughMoneyException() {
    Mockito.when(request.getHeader(SecurityConfiguration.ACCESS_TOKEN)).thenReturn("test");
    Mockito.when(jwtUtils.validateToken(Mockito.anyString())).thenReturn(
        new Account(0, "0", "0", 100.0, 0.0));
    Mockito.when(billRepository.findAll()).thenReturn(List.of(new Bill(0, 50.0, 1)));
    Mockito.when(billRepository.findAllByOrderByBillValueDesc()).thenReturn(List.of(
        new Bill(0, 50.0, 2)));
    assertThrows(NotEnoughMoneyException.class, () ->
        operationService.doWithdrawal(100.0)
    );
  }

}
