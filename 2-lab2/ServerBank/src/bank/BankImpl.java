//created by duknust
//find in https://github.com/Duknust
package bank;

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
