package ex4;

import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;

public class Util {
    public static <T> T makeRemote(ThreadContext tc, Reference<T> ref) throws Exception {
        Transport t = new NettyTransport();

        Connection c = tc.execute(() ->
                t.client().connect(new Address(ref.getAddress()))
        ).join().get();

        return ref.getType()
                  .getDeclaredConstructor(ThreadContext.class, Connection.class, Integer.class)
                  .newInstance(tc, c, ref.getId());
    }

    public static <T> Reference<T> makeReference(Address addr, int id, Class<T> type) {
        return new Reference<>(addr, id, type);
    }
}
