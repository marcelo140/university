import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.haslab.ekit.Clique;
import pt.haslab.ekit.Log;

public class Client {
    public static void main(String[] args) {
        Address[] addresses = new Address[] {
                new Address("127.0.0.1:5000"),
                new Address("127.0.0.1:5001"),
                new Address("127.0.0.1:5002"),
        };

        int id = Integer.parseInt(args[0]);
        Transport t = new NettyTransport();
        ThreadContext tc = new SingleThreadContext("proto-%d", new Serializer());

        Log l = new Log("log" + id);

        l.handler(Prepare.class, (sender, msg) -> {
            l.append(msg);
        });

        l.handler(Commit.class, (sender, msg) -> {
            l.append(msg);
        });

        l.open().thenRun(() -> {
            new Clique(t, id, addresses);
            // TODO: something useful
        });
    }
}
