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

public class Bank implements IBank {
    private static ThreadContext tc;
    private static Transport t;

    private int balance = 0;

    public static void main(String[] args) {
        t = new NettyTransport();
        tc = new SingleThreadContext("srv-%d", new Serializer());

        tc.serializer().register(BankBalanceReq.class);
        tc.serializer().register(BankBalanceRep.class);
        tc.serializer().register(BankMoveReq.class);
        tc.serializer().register(BankMoveRep.class);

        run(new Bank());
    }

    private static void run(IBank bank) {
        tc.execute(() -> {
            t.server().listen(new Address("localhost:5000"), c -> {
                c.handler(BankBalanceReq.class, req -> {
                    int balance = bank.balance();

                    return Futures.completedFuture(new BankBalanceRep(balance));
                });
                c.handler(BankMoveReq.class, req -> {
                    boolean res = bank.move(req.getAmount());

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
}
