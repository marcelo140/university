package ex3.stubs;

import ex3.com.StoreMakeCartRep;
import ex3.com.StoreMakeCartReq;
import ex3.com.StoreSearchRep;
import ex3.com.StoreSearchReq;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

public class RemoteStore {
    private final ThreadContext tc;
    private final Connection c;

    public RemoteStore() throws Exception {
        Transport t = new NettyTransport();
        tc = new SingleThreadContext("rs-%d", new Serializer());

        tc.serializer().register(StoreSearchReq.class);
        tc.serializer().register(StoreSearchRep.class);
        tc.serializer().register(StoreMakeCartReq.class);
        tc.serializer().register(StoreMakeCartRep.class);

        c = tc.execute(() ->
            t.client().connect(new Address("localhost:6000"))
        ).join().get();
    }

    public int search(String title) throws Exception {
        StoreSearchRep r = (StoreSearchRep) tc.execute(() ->
            c.sendAndReceive(new StoreSearchReq("East of Eden"))
        ).join().get();

        return r.getIsbn();
    }

    public RemoteCart newCart() throws Exception {
        StoreMakeCartRep makeCart = (StoreMakeCartRep) tc.execute(() ->
            c.sendAndReceive(new StoreMakeCartReq())
        ).join().get();

        return new RemoteCart(makeCart.getCartId());
    }
}
