package com.zinkworks.atm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zinkworks.atm.service.TokenService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TokenControllerTest {

  private static TokenService tokenService;
  private static TokenController tokenController;

  @BeforeAll
  private static void beforeAll() {
    tokenService = Mockito.mock(TokenService.class);
    tokenController = new TokenController(tokenService);
  }

  @Test
  @SneakyThrows
  void generateTokenTest() {
    Mockito.when(tokenService.generateToken()).thenReturn(ResponseEntity.ok(null));
    assertEquals(HttpStatus.OK, tokenController.getAccessToken().getStatusCode());
  }
}
