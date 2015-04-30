/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author duarteduarte
 */
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

            Operation op = null;

            while (!shutdown) {
                op = (Operation) brClient.readObject();
                System.out.println("RCV:" + op.toString() + "----");
                if (op.getOperation() == 1 || op.getOperation() == 2) {
                    int value = op.getAmount();
                    boolean result = this.bank.move(value);
                    op = new Operation(result == true ? 1 : 0);
                    bwClient.writeObject(op);
                } else if (op.getOperation() == 3) {
                    op = new Operation(3, this.bank.getBalance());
                    bwClient.writeObject(op);
                } else {
                    op = new Operation(true);
                    bwClient.writeObject(op);
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
