//created by duknust
//find in https://github.com/Duknust
package serverbank;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import bank.BankImpl;
import bank.Operation;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ClientHandler implements Runnable {

    Socket socket = null;
    BankImpl bank = null;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    ClientHandler(Socket socket, BankImpl bank) {
        this.socket = socket;
        this.bank = bank;
    }

    @Override
    public void run() {
        ObjectInput brClient = null;
        ObjectOutput bwClient = null;
        try {
            boolean shutdown = false;
            brClient = new ObjectInputStream(this.socket.getInputStream());
            bwClient = new ObjectOutputStream(this.socket.getOutputStream());

            Operation op;

            while (!shutdown) {
                op = (Operation) brClient.readObject();
                System.out.println("RCV:" + op.toString() + "----");
                if (op.getOperation() == Operation.Type.MOVE) {
                    int value = op.getAmount();
                    boolean result = this.bank.move(value);
                    op = new Operation(result);
                    bwClient.writeObject(op);
                } else if (op.getOperation() == Operation.Type.BALANCE) {
                    op = new Operation(Operation.Type.BALANCE, this.bank.getBalance());
                    bwClient.writeObject(op);
                } else {
                    bwClient.close();
                    brClient.close();
                    shutdown = true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (brClient != null) {
                    brClient.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
