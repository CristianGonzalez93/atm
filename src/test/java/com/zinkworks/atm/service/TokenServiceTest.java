package com.zinkworks.atm.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zinkworks.atm.config.SecurityConfiguration;
import com.zinkworks.atm.dto.UserDetailsDTO;
import com.zinkworks.atm.repository.domain.Account;
import com.zinkworks.atm.util.JwtUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TokenServiceTest {

  private static TokenService tokenService;
  private static JwtUtils jwtUtils;

  @BeforeAll
  private static void beforeAll() {
    jwtUtils = Mockito.mock(JwtUtils.class);
    tokenService = new TokenService(jwtUtils);
  }

  @Test
  @SneakyThrows
  void generateTokenTest() {
    Mockito.when(jwtUtils.generateToken(Mockito.any())).thenReturn("this is a test");
    try (MockedStatic<SecurityConfiguration> utilities = Mockito.mockStatic(SecurityConfiguration.class)) {
      utilities.when(SecurityConfiguration::getLoggedUser).thenReturn(new UserDetailsDTO(new Account()));
      assertTrue(!StringUtils.isEmpty(tokenService.generateToken().getBody()));
    }
  }

}
