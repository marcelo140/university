package ex3.stubs;

import ex3.com.CartAddRep;
import ex3.com.CartAddReq;
import ex3.com.CartBuyRep;
import ex3.com.CartBuyReq;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

public class RemoteCart {
    private final ThreadContext tc;
    private final Connection c;

    private int id;

    public RemoteCart(int id) throws Exception {
        this.id = id;

        Transport t = new NettyTransport();
        tc = new SingleThreadContext("rs-%d", new Serializer());

        tc.serializer().register(CartAddReq.class);
        tc.serializer().register(CartAddRep.class);
        tc.serializer().register(CartBuyReq.class);
        tc.serializer().register(CartBuyRep.class);

        c = tc.execute(() ->
                t.client().connect(new Address("localhost:6000"))
        ).join().get();
    }

    public void add(int isbn) throws Exception {
        tc.execute(() ->
                c.sendAndReceive(new CartAddReq(id, isbn))
        ).join().get();
    }

    public boolean buy() throws Exception {
        CartBuyRep r = (CartBuyRep) tc.execute(() ->
                c.sendAndReceive(new CartBuyReq())
        ).join().get();

        return r.isSuccess();
    }
}
