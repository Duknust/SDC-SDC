/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import bank.BankImpl;

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
        BufferedReader brClient = null;
        BufferedWriter bwClient = null;
        try {
            boolean shutdown = false;
            brClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            bwClient = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

            while (!shutdown) {
                String msg = brClient.readLine().trim();
                System.out.println("RCV:" + msg + "----");
                String[] stages = msg.split(" ");
                if (stages[0].equals("1") || stages[0].equals("2")) {
                    String op = stages[0];
                    String value = stages[1];
                    boolean result = this.bank.move(Integer.parseInt(value));
                    int res = result == true ? 1 : 0;
                    bwClient.write(res + "\n");
                    bwClient.flush();
                } else if (stages[0].equals("3")) {
                    System.out.println("tosend:" + this.bank.getBalance());
                    bwClient.write(this.bank.getBalance() + "\n");
                    bwClient.flush();
                } else {
                    bwClient.write("closed\n");
                    bwClient.flush();
                    bwClient.close();
                    brClient.close();
                    shutdown = true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                brClient.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
