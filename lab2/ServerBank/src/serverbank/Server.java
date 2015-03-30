//created by duknust
//find in https://github.com/Duknust
package serverbank;

import bank.BankImpl;
import communication.OpBalance;
import communication.OpLeave;
import communication.OpMove;
import communication.Operation;
import communication.ResBalance;
import communication.ResLeave;
import communication.ResMove;
import communication.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.DataSession;
import net.sf.jgcs.GroupException;
import net.sf.jgcs.Message;
import net.sf.jgcs.MessageListener;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.annotation.PointToPoint;
import net.sf.jgcs.ip.IpGroup;
import net.sf.jgcs.jgroups.JGroupsGroup;
import net.sf.jgcs.jgroups.JGroupsProtocolFactory;
import net.sf.jgcs.jgroups.JGroupsService;

public class Server implements MessageListener {

    static BankImpl bank = null;
    static DataSession ds = null;
    IpGroup gc = null;
    JGroupsGroup gg = null;
    Protocol p = null;

    public Server(IpGroup gc, Protocol p) {
        try {
            this.gc = gc;
            this.p = p;

            this.ds = p.openDataSession(gc);
            this.ds.setMessageListener(this);
            ControlSession cs = this.p.openControlSession(gc);
            cs.join();
        } catch (GroupException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Server(JGroupsGroup gg, Protocol p) {
        try {
            this.gg = gg;
            this.p = p;

            this.ds = p.openDataSession(gg);
            this.ds.setMessageListener(this);
            ControlSession cs = this.p.openControlSession(gg);
            cs.join();
        } catch (GroupException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        try {
            bank = new BankImpl();

            System.out.println("[Server] Server started");

            //IpProtocolFactory pf = new IpProtocolFactory();
            JGroupsProtocolFactory gf = new JGroupsProtocolFactory();
            JGroupsGroup gg = new JGroupsGroup("banco");

            Protocol p = gf.createProtocol();
            Server s = new Server(gg, p);

            //IpGroup gc = new IpGroup("225.1.2.3:12345");
            //Protocol p = pf.createProtocol();
            //Server s = new Server(gc, p);
            while (true) {
                Thread.sleep(100000);
            }

        } catch (GroupException | InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object onMessage(Message msg) {
        System.out.println("HERE COMES A MESSAGE");
        try {
            ObjectInputStream oisHere = null;
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(msg.getPayload());
                oisHere = new ObjectInputStream(bais);
                Operation op = (Operation) oisHere.readObject();
                Response res = null;
                if (op instanceof OpMove) {
                    int value = ((OpMove) op).getAmount();
                    boolean result = bank.move(value);
                    res = new ResMove(result);
                } else if (op instanceof OpBalance) {
                    res = new ResBalance(bank.getBalance());
                } else if (op instanceof OpLeave) {
                    res = new ResLeave();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oosHere = new ObjectOutputStream(baos);

                oosHere.writeObject(res);
                byte[] data = baos.toByteArray();
                Message toSend = ds.createMessage();
                toSend.setPayload(data);

                //ds.multicast(toSend, new IpService(), null, new PointToPoint(msg.getSenderAddress()));
                ds.multicast(toSend, new JGroupsService(), null, new PointToPoint(msg.getSenderAddress()));

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } /*finally {
             try {
             if (oisHere != null) {
             oisHere.close();
             }
             } catch (IOException ex) {
             Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
             }
             }*/

        } catch (ClassCastException ex) {
            System.out.println("discarding");
        }
        return null;
    }

}
