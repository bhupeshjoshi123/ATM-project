package com.progressoft.induction.atm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {

        ATM atm = new ATMImpl();

        List<Banknote> banknoteList = atm.withdraw("222222222", BigDecimal.valueOf(225));


        for (Banknote banknote : banknoteList) {
            System.out.println(banknote);
        }

    }


}
