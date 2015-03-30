//created by duknust
//find in https://github.com/Duknust
package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    public static void main(String[] args) {
        try {
            BankStub bank = new BankStub();

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
                bwConsole.write("#> ");
                bwConsole.flush();

                String opt = brConsole.readLine().trim();
                String amount;

                switch (opt) {
                    case "1":
                        bwConsole.write("[SYS] amount >");
                        bwConsole.flush();
                        amount = brConsole.readLine().trim();
                        bank.move(Integer.parseInt(amount));
                        System.out.println("SENT:" + opt + " " + amount + "----");
                        break;
                    case "2":
                        bwConsole.write("[SYS] amount >");
                        bwConsole.flush();
                        amount = brConsole.readLine().trim();
                        if (bank.move(0 - Integer.parseInt(amount))) {
                            bwConsole.write("[SYS] Success\n");
                            bwConsole.flush();
                        } else {
                            bwConsole.write("[SYS] Error\n");
                            bwConsole.flush();
                        }
                        break;
                    case "3":
                        bwConsole.write("[SYS] " + bank.getBalance() + "\n");
                        bwConsole.flush();
                        break;
                    case "4":
                        shutdown = true;
                        bank.leave();
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
