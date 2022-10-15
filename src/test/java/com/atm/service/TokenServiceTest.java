package com.atm.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.atm.config.SecurityConfiguration;
import com.atm.dto.UserDetailsDTO;
import com.atm.repository.domain.Account;
import io.github.gonguasp.jwt.service.JwtService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TokenServiceTest {

  private static TokenService tokenService;
  private static JwtService jwtService;

  @BeforeAll
  private static void beforeAll() {
    jwtService = Mockito.mock(JwtService.class);
    tokenService = new TokenService(jwtService);
  }

  @Test
  @SneakyThrows
  void generateTokenTest() {
    Mockito.when(jwtService.generateToken(Mockito.any(), Mockito.any())).thenReturn("this is a test");
    try (MockedStatic<SecurityConfiguration> utilities = Mockito.mockStatic(SecurityConfiguration.class)) {
      utilities.when(SecurityConfiguration::getLoggedUser).thenReturn(new UserDetailsDTO(new Account()));
      assertTrue(!StringUtils.isEmpty(tokenService.generateToken().getBody()));
    }
  }

}
