package com.atm.service;

import com.atm.config.SecurityConfiguration;
import com.atm.exception.NotEnoughBillsException;
import com.atm.exception.NotEnoughCashException;
import com.atm.exception.NotEnoughFundsException;
import com.atm.exception.NotEnoughMoneyException;
import com.atm.repository.AccountRepository;
import com.atm.repository.BillRepository;
import com.atm.repository.domain.Account;
import com.atm.repository.domain.Bill;
import com.atm.util.JwtUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
@AllArgsConstructor
public class OperationService {

  private final HttpServletRequest request;

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private JwtUtils jwtUtils;

  public ResponseEntity<Map<String, Object>> checkBalance() {
    Account account = jwtUtils.validateToken(request.getHeader(SecurityConfiguration.ACCESS_TOKEN));
    Map<String, Object> balance = new HashMap<>();
    balance.put("balance", account.getBalance());
    balance.put("max withdrawal", getCurrentMaxWithdrawal(account.getBalance()));
    log.info("Checked balance: " + balance);
    return ResponseEntity.ok(balance);
  }

  public ResponseEntity<Map<String, Object>> doWithdrawal(Double withdrawal)
      throws NotEnoughCashException {
    Account account = jwtUtils.validateToken(request.getHeader(SecurityConfiguration.ACCESS_TOKEN));
    List<Bill> notes = new ArrayList<>();
    if (withdrawal > account.getBalance()) {
      throw new NotEnoughFundsException("It is not possible to do withdrawal of " + withdrawal +
          " because you do not have enough cash in your account.");
    } else if (withdrawal > getMaxFunds()) {
      throw new NotEnoughMoneyException("It is not possible to do withdrawal of " + withdrawal +
          " because there are not enough money in the ATM machine.");
    } else if (isWithdrawalAble(withdrawal.doubleValue(), notes)) {
      account.setBalance(account.getBalance() - withdrawal);
      accountRepository.save(account);
      log.info("Doing withdrawal of " + withdrawal);
      return ResponseEntity.ok(
          Map.of("withdrawal", withdrawal,
          "remaining balance", account.getBalance(),
          "notes", notes));
    } else {
      throw new NotEnoughBillsException("It is not possible to do withdrawal of " + withdrawal +
          " because there are not enough bills in the ATM machine.");
    }
  }

  private boolean isWithdrawalAble(double withdrawal, List<Bill> notes) {
    List<Bill> bills = billRepository.findAllByOrderByBillValueDesc();
    Gson gson = new Gson();
    List<Bill> billsCloned = gson.fromJson(gson.toJson(bills), new TypeToken<List<Bill>>(){}.getType());
    List<Bill> modifiedBillsAmount = new ArrayList<>();

    for (Bill bill : bills) {
      boolean modified = false;
      while (bill.getAmount() > 0 && withdrawal > 0) {
        if (bill.getAmount() > 0 && withdrawal >= bill.getBillValue()) {
          withdrawal -= bill.getBillValue();
          bill.setAmount(bill.getAmount() - 1);
          modified = true;
        } else {
          break;
        }
      }
      if (modified) {
        modifiedBillsAmount.add(bill);
      }
    }

    return updateBillsAmount(withdrawal, notes, billsCloned, modifiedBillsAmount);
  }

  private boolean updateBillsAmount(double withdrawal, List<Bill> notes, List<Bill> billsCloned, List<Bill> modifiedBillsAmount) {
    if (withdrawal == 0) {
      for (Bill modifiedBill : modifiedBillsAmount) {
        Bill originalBill = billsCloned.stream()
            .filter(bill -> modifiedBill.getId() == bill.getId()).findFirst().get();
        notes.add(new Bill(
            modifiedBill.getBillValue(), originalBill.getAmount() - modifiedBill.getAmount()));
        billRepository.save(modifiedBill);
      }
      return true;
    } else {
      return false;
    }
  }

  private Double getCurrentMaxWithdrawal(Double balance) {
    Double maxFunds = getMaxFunds();
    return balance < maxFunds ? balance : maxFunds;
  }

  private Double getMaxFunds() {
    AtomicReference<Double> funds = new AtomicReference<>(0.0);
    billRepository.findAll().forEach(bill ->
      funds.updateAndGet(v -> v + bill.getBillValue() * bill.getAmount())
    );

    return funds.get();
  }

}
