package com.atm.service;

import com.atm.config.SecurityConfiguration;
import com.atm.dto.UserDetailsDTO;
import com.atm.exception.NotValidUserException;
import com.atm.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {

  @Autowired
  private JwtUtils jwtUtils;

  public ResponseEntity<String> generateToken() throws NotValidUserException {
    return ResponseEntity.ok(jwtUtils.generateToken((UserDetailsDTO) SecurityConfiguration.getLoggedUser()));
  }
}
