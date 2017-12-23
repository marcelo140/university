package ex4.stubs;

import ex4.com.CartAddRep;
import ex4.com.CartAddReq;
import ex4.com.CartBuyRep;
import ex4.com.CartBuyReq;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.transport.Connection;

public class RemoteCart {
    private final ThreadContext tc;
    private final Connection c;

    private int id;

    public RemoteCart(ThreadContext tc, Connection c, Integer id) {
        this.tc = tc;
        this.c = c;
        this.id = id;

        tc.serializer().register(CartAddReq.class);
        tc.serializer().register(CartAddRep.class);
        tc.serializer().register(CartBuyReq.class);
        tc.serializer().register(CartBuyRep.class);
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
