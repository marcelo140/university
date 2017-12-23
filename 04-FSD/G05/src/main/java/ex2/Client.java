package ex2;

import ex2.com.*;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

public class Client {
    public static void main(String[] args) throws Exception {
        Transport t = new NettyTransport();
        ThreadContext tc = new SingleThreadContext("tc-%d", new Serializer());

        tc.serializer().register(StoreSearchReq.class);
        tc.serializer().register(StoreSearchRep.class);
        tc.serializer().register(StoreMakeCartReq.class);
        tc.serializer().register(StoreMakeCartRep.class);
        tc.serializer().register(CartAddReq.class);
        tc.serializer().register(CartAddRep.class);
        tc.serializer().register(CartBuyReq.class);
        tc.serializer().register(CartBuyRep.class);

        System.out.println("Trying to connect");

        Connection c = tc.execute(() ->
            t.client().connect(new Address("127.0.0.1:6000"))
        ).join().get();

        System.out.println("Connected!\nLooking for book");

        StoreSearchRep search = (StoreSearchRep) tc.execute(() ->
            c.sendAndReceive(new StoreSearchReq("East of Eden"))
        ).join().get();

        System.out.println("Book found");

        int bookIsbn = search.getIsbn();

        StoreMakeCartRep makeCart = (StoreMakeCartRep) tc.execute(() ->
            c.sendAndReceive(new StoreMakeCartReq())
        ).join().get();

        System.out.println("Cart created");
        int cartId = makeCart.getCartId();

        tc.execute(() ->
            c.sendAndReceive(new CartAddReq(cartId, bookIsbn))
        ).join().get();

        System.out.println("Book added to cart");

        CartBuyRep purchase = (CartBuyRep) tc.execute(() ->
            c.sendAndReceive(new CartBuyReq(cartId))
        ).join().get();

        System.out.println("The purchase was a success: " + purchase.isSuccess());
    }
}
