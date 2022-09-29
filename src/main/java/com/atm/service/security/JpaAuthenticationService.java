package com.atm.service.security;

import com.atm.dto.UserDetailsDTO;
import com.atm.repository.AccountRepository;
import com.atm.repository.domain.Account;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaAuthenticationService implements UserDetailsService {

  @Autowired
  @NonNull
  private AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
    Account account = accountRepository.findByAccountNumber(accountNumber);

    if (account == null) {
      throw new UsernameNotFoundException("Account not found: " + accountNumber);
    }

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("USER"));

    UserDetailsDTO userDetailsDTO = new UserDetailsDTO(account);
    userDetailsDTO.setAuthorities(grantedAuthorities);

    return userDetailsDTO;
  }

}
