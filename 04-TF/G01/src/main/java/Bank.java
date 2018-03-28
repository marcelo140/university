import com.BankBalanceRep;
import com.BankBalanceReq;
import com.BankMoveRep;
import com.BankMoveReq;
import ifaces.IBank;
import io.atomix.catalyst.concurrent.Futures;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.haslab.ekit.Clique;

public class Bank implements IBank {
    private static ThreadContext tc, tcc;
    private static Transport t;
    private static Clique cliq;
    private static int id;

    private int balance = 0;

    public static void main(String[] args) {
        t = new NettyTransport();
        tc = new SingleThreadContext("srv-%d", new Serializer());

        tc.serializer().register(BankBalanceReq.class);
        tc.serializer().register(BankBalanceRep.class);
        tc.serializer().register(BankMoveReq.class);
        tc.serializer().register(BankMoveRep.class);

        id = Integer.parseInt(args[0]);
        Bank b = new Bank();
        setInnerNetwork(b, id);
    }

    private static void run(IBank bank) {
        tc.execute(() -> {
            t.server().listen(new Address("localhost", 5000+id), c -> {
                c.handler(BankBalanceReq.class, req -> {
                    int balance = bank.balance();

                    return Futures.completedFuture(new BankBalanceRep(balance));
                });
                c.handler(BankMoveReq.class, req -> {
                    boolean res = bank.move(req.getAmount());
                    tcc.execute(() -> {
                        cliq.send(1-id, bank.balance());
                    });

                    return Futures.completedFuture(new BankMoveRep(res));
                });
            });
        });
    }

    public int balance() {
        return balance;
    }

    public synchronized boolean move(int amount) {
        if (amount >= 0 || balance - amount > 0) {
            balance += amount;
            return true;
        }

        return false;
    }

    public synchronized void setBalance(int amount) {
        balance = amount;
    }

    private static void setInnerNetwork(Bank b, int id) {
        Transport tt = new NettyTransport();
        tcc = new SingleThreadContext("tcc-%d", new Serializer());
        Address[] addresses = new Address[]{
                new Address("localhost:5010"),
                new Address("localhost:5011")
        };

        cliq = new Clique(tt, Clique.Mode.ALL, id, addresses);
        tcc.execute(() -> {
            cliq.handler(Integer.class, (_r, m) -> {
                b.setBalance(m);
            });

            cliq.open().thenRun(() -> {
                System.out.println("Clique connected!");
                run(b);
            });
        }).join();
    }
}
