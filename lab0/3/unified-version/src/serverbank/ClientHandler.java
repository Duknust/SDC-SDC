//created by duknust
//find in https://github.com/Duknust
package serverbank;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import bank.BankImpl;
import communication.OpBalance;
import communication.OpLeave;
import communication.OpMove;
import communication.Operation;
import communication.ResBalance;
import communication.ResMove;
import communication.Response;
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
            Response res;

            while (!shutdown) {
                op = (Operation) brClient.readObject();
                System.out.println("RCV:" + op.toString() + "----");
                if (op instanceof OpMove) {
                    int value = ((OpMove) op).getAmount();
                    boolean result = this.bank.move(value);
                    res = new ResMove(result);
                    bwClient.writeObject(res);
                } else if (op instanceof OpBalance) {
                    res = new ResBalance(this.bank.getBalance());
                    bwClient.writeObject(res);
                } else if (op instanceof OpLeave) {
                    bwClient.close();
                    brClient.close();
                    shutdown = true;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
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
