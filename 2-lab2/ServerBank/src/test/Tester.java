//created by duknust
//find in https://github.com/Duknust
package test;

import bank.BankImpl;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tester {

    public static void main(String[] args) {
        BankImpl bank = new BankImpl();
        ArrayList<Worker> workers = new ArrayList<>();
        int i = 0;
        for (i = 0; i < 10; i++) {
            Worker wk = new Worker(bank);
            wk.run();
            workers.add(wk);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(bank.getBalance());
    }
}

class Worker implements Runnable {

    BankImpl bank = null;

    public Worker(BankImpl bank) {
        this.bank = bank;
    }

    @Override
    public void run() {
        int moves = 0;
        for (int i = 0; i < 100000; i++) {
            Random rand = new Random();
            int value = rand.nextInt(10000);
            if (value < 5000) {
                value = 0 - value;
            }
            boolean status = bank.move(value);
            if (status) {
                moves += value;
            }
        }
        System.out.println("MOVES:" + moves);
    }

}
