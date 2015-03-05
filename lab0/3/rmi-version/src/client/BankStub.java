//created by duknust
//find in https://github.com/Duknust
package client;

import bank.Operation;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankStub {

    Socket s;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public BankStub() {
        try {
            String host = "localhost";
            int port = 4567;
            this.s = new Socket(host, port);
            oos = new ObjectOutputStream(this.s.getOutputStream());
            ois = new ObjectInputStream(this.s.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getBalance() {
        int balance = -1;
        try {
            Operation r = new Operation(Operation.Type.BALANCE);
            oos.writeObject(r);
            oos.flush();

            balance = ((Operation) ois.readObject()).getAmount();
        } catch (IOException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }

    public boolean move(int amount) {
        boolean result = false;
        try {
            Operation r = new Operation(Operation.Type.MOVE, amount);
            oos.writeObject(r);
            oos.flush();

            result = ((Operation) ois.readObject()).getStatus();
        } catch (IOException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void leave() {
        try {
            oos.writeObject(new Operation(Operation.Type.LEAVE));
            ois.close();
            oos.close();
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
