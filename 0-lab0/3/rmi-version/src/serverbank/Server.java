//created by duknust
//find in https://github.com/Duknust
package serverbank;

import bank.BankImpl;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    static BankImpl bank = null;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            bank = new BankImpl();
            System.out.println("[Server] Server started");
            while (true) {
                Socket socket = ss.accept();
                ClientHandler ch = new ClientHandler(socket, bank);
                ch.run();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
