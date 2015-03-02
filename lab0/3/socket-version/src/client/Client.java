/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

            BufferedReader brServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bwServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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

                switch (opt) {
                    case "1":
                        bwConsole.write("[SYS] amount >\n");
                        bwConsole.flush();
                        amount = brConsole.readLine().trim();
                        bwServer.write(opt + " " + amount + "\n");
                        bwServer.flush();
                        System.out.println("SENT:" + opt + " " + amount + "----");
                        break;
                    case "2":
                        bwConsole.write("[SYS] amount >\n");
                        bwConsole.flush();
                        amount = brConsole.readLine().trim();
                        bwServer.write(opt + " " + amount + "\n");
                        bwServer.flush();
                        if (brServer.readLine().trim().equals("1")) {
                            bwConsole.write("[SYS] Success\n");
                            bwConsole.flush();
                        } else {
                            bwConsole.write("[SYS] Error\n");
                            bwConsole.flush();
                        }
                        ;
                        System.out.println("SENT:" + opt + " " + amount + "----");
                        break;
                    case "3":
                        bwServer.write(opt + "\n");
                        bwServer.flush();
                        response = brServer.readLine();
                        System.out.println("res:" + response);
                        bwConsole.write("[SYS] " + response + "\n");
                        bwConsole.flush();
                        break;
                    case "4":
                        shutdown = true;
                        bwServer.write("4\n");
                        bwServer.flush();
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
        }

    }
}
