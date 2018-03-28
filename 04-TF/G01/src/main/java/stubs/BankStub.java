package stubs;

import com.BankBalanceRep;
import com.BankBalanceReq;
import com.BankMoveRep;
import com.BankMoveReq;
import ifaces.IBank;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

public class BankStub implements IBank {
    private final ThreadContext tc;
    private final Connection c;

    public BankStub(int id) throws Exception {
        Transport t = new NettyTransport();
        tc = new SingleThreadContext("tc-%d", new Serializer());

        tc.serializer().register(BankBalanceReq.class);
        tc.serializer().register(BankBalanceRep.class);
        tc.serializer().register(BankMoveReq.class);
        tc.serializer().register(BankMoveRep.class);

        c = tc.execute(() ->
                t.client().connect(new Address("localhost", 5000+id))
        ).join().get();
    }

    @Override
    public int balance() {
        try {
            BankBalanceRep r = (BankBalanceRep) tc.execute(() ->
                    c.sendAndReceive(new BankBalanceReq())
            ).join().get();

            return r.getAmount();
        } catch(Exception e) {
            System.out.println("Bank is down");
            return 0;
        }
    }

    @Override
    public boolean move(int amount) {
        try {
            BankMoveRep r = (BankMoveRep) tc.execute(() ->
                    c.sendAndReceive(new BankMoveReq(amount))
            ).join().get();

            return r.isRes();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
