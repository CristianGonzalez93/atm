package com.zinkworks.atm.service;

import com.zinkworks.atm.config.SecurityConfiguration;
import com.zinkworks.atm.dto.UserDetailsDTO;
import com.zinkworks.atm.exception.NotValidUserException;
import com.zinkworks.atm.util.JwtUtils;
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
