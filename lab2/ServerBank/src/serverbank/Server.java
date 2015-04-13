//created by duknust
//find in https://github.com/Duknust
package serverbank;

import bank.BankImpl;
import com.sun.jmx.remote.internal.ArrayQueue;
import communication.OpBalance;
import communication.OpLeave;
import communication.OpMove;
import communication.OpRecover;
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

    private static boolean startingFromZero = true;

    static BankImpl bank = null;
    static DataSession ds = null;
    static ControlSession cs = null;
    ArrayQueue<Message> queue = null;

    IpGroup gc = null;
    JGroupsGroup gg = null;
    Protocol p = null;

    static int estado = 1;

    //ip
    public Server(IpGroup gc, Protocol p) {
        try {
            this.gc = gc;
            this.p = p;

            ds = p.openDataSession(gc);
            ds.setMessageListener(this);
            cs = this.p.openControlSession(gc);
            cs.join();
        } catch (GroupException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //group
    public Server(JGroupsGroup gg, Protocol p) {
        try {
            this.gg = gg;
            this.p = p;

            ds = p.openDataSession(gg);
            ds.setMessageListener(this);
            cs = this.p.openControlSession(gg);
            cs.join();

            this.queue = new ArrayQueue<>(100);
        } catch (GroupException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        try {

            //IpProtocolFactory pf = new IpProtocolFactory();
            JGroupsProtocolFactory gf = new JGroupsProtocolFactory();

            //IpGroup gc = new IpGroup("225.1.2.3:12345");
            //Protocol p = pf.createProtocol();
            //Server s = new Server(gc, p);
            JGroupsGroup gg = new JGroupsGroup("banco");
            Protocol p = gf.createProtocol();
            Server s = new Server(gg, p);

            if (startingFromZero) {
                bank = new BankImpl();
            } else {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);

                    Operation r = new OpRecover();
                    oos.writeObject(r);
                    byte[] data = baos.toByteArray();

                    Message msg = ds.createMessage();
                    msg.setPayload(data);

                    //ds.multicast(msg, new IpService(), null);
                    ds.multicast(msg, new JGroupsService(), null);

                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("[Server] Server started");
            while (true) {
                Thread.sleep(100000);
            }

        } catch (GroupException | InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object onMessage(Message msg
    ) {

        System.out.println("HERE COMES A MESSAGE");
        switch (estado) {
            case 1:
                if (msg.getSenderAddress().equals(cs.getLocalAddress())) {
                    estado = 2;
                }
                break;
            case 2:
                try {
                    ObjectInputStream oisHere = null;
                    ByteArrayInputStream bais = new ByteArrayInputStream(msg.getPayload());
                    oisHere = new ObjectInputStream(bais);
                    Operation op = (Operation) oisHere.readObject();
                    if (op instanceof OpRecover) {
                        estado = 3;
                        bank = new BankImpl();
                        for (int i = 0; i < this.queue.size(); i++) {
                            apply(this.queue.remove(i));
                        }
                    } else if (op instanceof OpMove) {
                        this.queue.add(msg);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 3:
                apply(msg);
                break;
        }

        return null;
    }

    private void apply(Message msg) {
        try {
            ObjectInputStream oisHere = null;
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
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
