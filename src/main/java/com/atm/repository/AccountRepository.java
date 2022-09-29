package com.atm.repository;

import com.atm.repository.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Account findByAccountNumber(String userName);
}
