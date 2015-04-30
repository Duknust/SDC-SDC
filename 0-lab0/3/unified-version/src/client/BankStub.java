//created by duknust
//find in https://github.com/Duknust
package client;

import communication.OpBalance;
import communication.OpLeave;
import communication.OpMove;
import communication.Operation;
import communication.ResBalance;
import communication.ResMove;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankStub implements bank.Bank {

    Socket s;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public BankStub() {
        try {
            String host = "localhost";
            int port = 12345;
            this.s = new Socket(host, port);
            oos = new ObjectOutputStream(this.s.getOutputStream());
            ois = new ObjectInputStream(this.s.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public int getBalance() {
        int balance = -1;
        try {
            Operation r = new OpBalance();
            oos.writeObject(r);
            oos.flush();

            balance = ((ResBalance) ois.readObject()).getAmount();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }

    @Override
    public boolean move(int amount) {
        boolean result = false;
        try {
            Operation r = new OpMove(amount);
            oos.writeObject(r);
            oos.flush();

            result = ((ResMove) ois.readObject()).getStatus();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void leave() {
        try {
            oos.writeObject(new OpLeave());
            ois.close();
            oos.close();
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
