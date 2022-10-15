package com.atm.service;

import com.atm.config.SecurityConfiguration;
import com.atm.dto.UserDetailsDTO;
import com.atm.exception.NotValidUserException;
import io.github.gonguasp.jwt.service.JwtService;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {

  private final JwtService jwtService;

  public ResponseEntity<String> generateToken() throws NotValidUserException {
    UserDetailsDTO userDetailsDTO = (UserDetailsDTO) SecurityConfiguration.getLoggedUser();
    Map<String, Object> claims = new HashMap<>();
    claims.put(OperationService.USER_ID, userDetailsDTO.getId());
    return ResponseEntity.ok(jwtService.generateToken(claims, userDetailsDTO.getUsername()));
  }
}
