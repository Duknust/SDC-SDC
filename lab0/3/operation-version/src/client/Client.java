/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import bank.Operation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duarteduarte
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            ObjectInputStream brServer = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream bwServer = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader brConsole = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bwConsole = new BufferedWriter(new OutputStreamWriter(System.out));

            boolean shutdown = false;

            while (!shutdown) {
                boolean status = true;

                bwConsole.write("=MENU=\n");
                bwConsole.flush();
                bwConsole.write("#1 Deposit money\n");
                bwConsole.flush();
                bwConsole.write("#2 Withdraw money\n");
                bwConsole.flush();
                bwConsole.write("#3 Bank balance\n");
                bwConsole.flush();
                bwConsole.write("#4 Shutdown\n");
                bwConsole.flush();

                String opt = brConsole.readLine().trim();
                String amount;
                String response;

                Operation op = null;

                switch (opt) {
                    case "1":
                        bwConsole.write("[SYS] amount >\n");
                        bwConsole.flush();
                        amount = brConsole.readLine().trim();
                        op = new Operation(1, Integer.parseInt(amount));
                        bwServer.writeObject(op);
                        System.out.println("SENT:" + opt + " " + amount + "----");
                        break;
                    case "2":
                        bwConsole.write("[SYS] amount >\n");
                        bwConsole.flush();
                        amount = brConsole.readLine().trim();
                        op = new Operation(1, Integer.parseInt(amount));
                        bwServer.writeObject(op);
                        op = (Operation) brServer.readObject();
                        System.out.println("SENT:" + opt + " " + amount + "----");
                        if (op.getStatus()) {
                            bwConsole.write("[SYS] Success\n");
                            bwConsole.flush();
                        } else {
                            bwConsole.write("[SYS] Error\n");
                            bwConsole.flush();
                        }
                        ;
                        break;
                    case "3":
                        op = new Operation(3);
                        bwServer.writeObject(op);
                        op = (Operation) brServer.readObject();
                        bwConsole.write("[SYS] " + op.getAmount() + "\n");
                        bwConsole.flush();
                        break;
                    case "4":
                        shutdown = true;
                        op = new Operation(4);
                        bwServer.writeObject(op);
                        bwConsole.write("[SYS] See you next time\n");
                        bwConsole.flush();
                        break;
                    default:
                        bwConsole.write("[SYS] Something went wrong\n");
                        bwConsole.flush();
                        status = false;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
