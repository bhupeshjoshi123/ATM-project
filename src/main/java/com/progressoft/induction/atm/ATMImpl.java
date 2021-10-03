package com.progressoft.induction.atm;


import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.InvalidAmountException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;

import java.math.BigDecimal;
import java.util.*;

public class ATMImpl implements ATM {


    private List<Banknote> banknotes = new ArrayList<>();

    private Map<Banknote, BigDecimal> banknoteQuantity = new HashMap<>();

    private BankSystemImpl bankSystem = new BankSystemImpl();

    private BigDecimal totalAmountInATM;


    public ATMImpl() {

        this.banknoteQuantity.put(Banknote.FIFTY_JOD, BigDecimal.valueOf(10));
        this.banknoteQuantity.put(Banknote.TWENTY_JOD, BigDecimal.valueOf(20));
        this.banknoteQuantity.put(Banknote.TEN_JOD, BigDecimal.valueOf(100));
        this.banknoteQuantity.put(Banknote.FIVE_JOD, BigDecimal.valueOf(100));

        this.totalAmountInATM = getTotalAmountInATM();

    }


    private BigDecimal getTotalAmountInATM() {

        List<Banknote> banknoteList = Arrays.asList(Banknote.values());
        BigDecimal totalAmountInATM = BigDecimal.valueOf(0);
        for (Banknote banknote : banknoteList) {
            BigDecimal multiple = banknoteQuantity.get(banknote).multiply(banknote.getValue());
            totalAmountInATM = totalAmountInATM.add(multiple);
        }
        return totalAmountInATM;

    }


    @Override
    public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {


        throwInvalidAmountException(amount);

        BigDecimal accountBalance = bankSystem.getAccountBalance(accountNumber);

        throwInsufficientFundsException(accountBalance, amount);

        throwNotEnoughMoneyInATMException(getTotalAmountInATM(), amount);

        addBanknotes(amount);

        bankSystem.debitAccount(accountNumber, amount);

        return banknotes;

    }

    private void throwInsufficientFundsException(BigDecimal accountBalance, BigDecimal amount) {
        if (accountBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
    }

    private void throwInvalidAmountException(BigDecimal amount) {
        List<Banknote> banknoteList = Arrays.asList(Banknote.values());

        Boolean checkRem = false;

        for (Banknote banknote : banknoteList) {
            if ((amount.remainder(banknote.getValue())).compareTo(BigDecimal.valueOf(0)) == 0) {
                checkRem = true;
            }
        }
        if (!checkRem) {
            throw new InvalidAmountException();
        }
    }

    private void addBanknotes(BigDecimal amount) {

        List<Banknote> banknoteList = Arrays.asList(Banknote.values());
        final int MINAMT_FOR_MIXNOTES = banknoteList.get(0).getValue().intValue() * 3;
        int intAmount = amount.intValue();
        int banknoteNums = 0;

        for (Banknote banknote : banknoteList) {

            if (banknoteQuantity.get(banknote).compareTo(BigDecimal.valueOf(0)) > 0) {
                int banknoteVal = banknote.getValue().intValue();
                int exceededBanknoteQuant = Math.max(intAmount / banknoteVal - banknoteQuantity.get(banknote).intValue(), 0);

                if (banknote.equals(banknoteList.get(0))) {
                    boolean checker = true;
                    for (int i = 1; i < banknoteList.size() - 1; i++) {
                        if (banknoteQuantity.get(banknoteList.get(i)).intValue() > 0) {
                            if ((intAmount <= MINAMT_FOR_MIXNOTES)) {
                                checker = false;
                            }
                        } else {
                            checker = true;
                        }

                    }
                    if (!checker) {
                        continue;
                    }
                }
                banknoteNums = intAmount / banknoteVal - exceededBanknoteQuant;

                int num = 0;
                for (int i = 0; i < banknoteNums; i++) {
                    banknotes.add(banknote);
                    num++;
                }

                banknoteQuantity.put(banknote, banknoteQuantity.get(banknote).subtract(BigDecimal.valueOf(num)));

                intAmount = (intAmount % banknoteVal) + ((exceededBanknoteQuant) * banknoteVal);

            }

        }
    }

    private void throwNotEnoughMoneyInATMException(BigDecimal totalAmountInATM, BigDecimal amount) {
        if (totalAmountInATM.compareTo(amount) < 0) {
            throw new NotEnoughMoneyInATMException();
        }
    }
}
