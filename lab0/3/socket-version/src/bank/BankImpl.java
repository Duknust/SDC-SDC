/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import bank.Bank;

/**
 *
 * @author duarteduarte
 */
public class BankImpl implements Bank {

    int balance = 0;

    @Override
    public synchronized boolean move(int value) {
        if (balance < 0) {
            System.out.println("THERE'S AN ERROR!");
            return false;
        }

        balance += value;
        if (balance < 0) {
            balance -= value;
            return false;
        }

        return true;
    }

    @Override
    public synchronized int getBalance() {
        int res = balance;
        return res;
    }
}
