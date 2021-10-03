package com.progressoft.induction.atm;

import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BankSystemImpl implements BankingSystem {


    private Map<String, BigDecimal> accounts = new HashMap<>();

    public BankSystemImpl() {
        this.accounts.put("123456789", BigDecimal.valueOf(1000.0));
        this.accounts.put("111111111", BigDecimal.valueOf(1000.0));
        this.accounts.put("222222222", BigDecimal.valueOf(1000.0));
        this.accounts.put("333333333", BigDecimal.valueOf(1000.0));
        this.accounts.put("444444444", BigDecimal.valueOf(1000.0));
    }


    @Override
    public BigDecimal getAccountBalance(String accountNumber) {
        throwExceptionIfInvalidAccNumber(accountNumber);
        return this.accounts.get(accountNumber);
    }

    @Override
    public void debitAccount(String accountNumber, BigDecimal amount) {

        throwExceptionIfInvalidAccNumber(accountNumber);

        BigDecimal accountBanlance = this.accounts.get(accountNumber);
        throwInsufficientFundsException(accountBanlance, amount);

        accountBanlance = accountBanlance.subtract(amount);
        updateAccountBalance(accountNumber, accountBanlance);
    }

    private void updateAccountBalance(String accountNumber, BigDecimal accountBanlance) {
        accounts.put(accountNumber, accountBanlance);

    }

    private void throwInsufficientFundsException(BigDecimal accountBanlance, BigDecimal amount) {
        if (accountBanlance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
    }

    private void throwExceptionIfInvalidAccNumber(String accountNumber) {
        if (!this.accounts.containsKey(accountNumber)) {
            throw new AccountNotFoundException();
        }
    }
}

