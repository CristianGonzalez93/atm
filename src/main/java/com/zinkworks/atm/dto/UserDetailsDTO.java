package com.zinkworks.atm.dto;

import com.zinkworks.atm.repository.domain.Account;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserDetailsDTO implements UserDetails {
  private static final long serialVersionUID = 1L;
  private long id;
  private String userName;
  private String password;
  private Double balance;
  private Double overdraft;
  private boolean active;
  private List<GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.active;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.active;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.active;
  }

  @Override
  public boolean isEnabled() {
    return this.active;
  }

  public UserDetailsDTO(Account account) {
    this.id = account.getId();
    this.userName = account.getAccountNumber();
    this.password = account.getPin();
    this.balance = account.getBalance();
    this.overdraft = account.getOverdraft();
    this.authorities = new ArrayList<>();
    this.active = true;
  }
}