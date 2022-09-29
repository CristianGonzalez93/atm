package com.zinkworks.atm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zinkworks.atm.dto.UserDetailsDTO;
import com.zinkworks.atm.exception.InvalidCredentialsException;
import com.zinkworks.atm.repository.AccountRepository;
import com.zinkworks.atm.repository.domain.Account;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JwtUtilsTest {

  private static AccountRepository accountRepository;
  private static JwtUtils jwtUtils;

  @BeforeAll
  private static void beforeAll() {
    accountRepository = Mockito.mock(AccountRepository.class);
    jwtUtils = new JwtUtils();
    jwtUtils.setAccountRepository(accountRepository);
  }

  @Test
  void generateTokenTest() {
    assertTrue(!jwtUtils.generateToken(new UserDetailsDTO(new Account())).isEmpty());
  }

  @Test
  void validateTokenTest() {
    long id = 1L;
    Account account = new Account();
    account.setId(id);
    Mockito.reset(accountRepository);
    Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(
        Optional.of(account));

    assertEquals(id, jwtUtils.validateToken(jwtUtils.generateToken(new UserDetailsDTO(account))).getId());
  }

  @Test
  void validateTokenTestExpiredJwtException() {
    Mockito.reset(accountRepository);
    Mockito.when(accountRepository.findById(Mockito.anyLong())).thenThrow(
        new ExpiredJwtException(null, null, ""));

    assertThrows(ExpiredJwtException.class, () ->
        jwtUtils.validateToken(jwtUtils.generateToken(new UserDetailsDTO(new Account()))).getId());
  }

  @Test
  void validateTokenTestInvalidCredentialsException() {
    Mockito.reset(accountRepository);
    Mockito.when(accountRepository.findById(Mockito.anyLong())).thenThrow(
        new InvalidCredentialsException(""));

    assertThrows(InvalidCredentialsException.class, () ->
        jwtUtils.validateToken(jwtUtils.generateToken(new UserDetailsDTO(new Account()))).getId());
  }

}
