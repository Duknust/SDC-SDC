//created by duknust
//find in https://github.com/Duknust
package client;

import communication.OpBalance;
import communication.OpLeave;
import communication.OpMove;
import communication.Operation;
import communication.ResBalance;
import communication.ResMove;
import communication.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.Message;
import net.sf.jgcs.MessageListener;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.ip.IpGroup;
import net.sf.jgcs.ip.IpProtocolFactory;
import net.sf.jgcs.ip.IpService;

public class BankStub implements bank.Bank, MessageListener {

    Socket s;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    DataSession ds = null;
    Response response = null;
    ByteArrayOutputStream baos = null;

    public BankStub() {
        try {
            IpProtocolFactory pf = new IpProtocolFactory();
            IpGroup gc = new IpGroup("225.1.2.3:12345");
            Protocol p = pf.createProtocol();
            this.ds = p.openDataSession(gc);
            this.ds.setMessageListener(this);

        } catch (IOException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized int getBalance() {
        int res = -1;
        try {
            this.baos = new ByteArrayOutputStream();
            this.oos = new ObjectOutputStream(this.baos);

            Operation r = new OpBalance();
            this.oos.writeObject(r);
            byte[] data = baos.toByteArray();

            Message msg = ds.createMessage();
            msg.setPayload(data);
            this.response = null;
            ds.multicast(msg, new IpService(), null);

            while (this.response == null) {
                wait();
            }

            res = ((ResBalance) this.response).getAmount();

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    @Override
    public synchronized boolean move(int amount) {
        boolean res = false;

        try {
            this.baos = new ByteArrayOutputStream();
            this.oos = new ObjectOutputStream(this.baos);

            Operation r = new OpMove(amount);
            this.oos.writeObject(r);
            byte[] data = baos.toByteArray();

            Message msg = ds.createMessage();
            msg.setPayload(data);
            this.response = null;
            ds.multicast(msg, new IpService(), null);

            while (this.response == null) {
                wait();
            }

            res = ((ResMove) this.response).getStatus();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public synchronized void leave() {
        try {
            oos.writeObject(new OpLeave());
            ois.close();
            oos.close();
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object onMessage(Message msg) {

        ObjectInputStream oisHere = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(msg.getPayload());
            oisHere = new ObjectInputStream(bais);
            this.response = (Response) oisHere.readObject();
            notify();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (oisHere != null) {
                    oisHere.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(BankStub.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
