import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;

import io.atomix.catalyst.serializer.Serializer;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;

import io.atomix.catalyst.transport.netty.NettyTransport;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import pt.haslab.ekit.Clique;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        Address[] addresses = {
            new Address("127.0.0.1:1240"),
            new Address("127.0.0.1:1241"),
            new Address("127.0.0.1:1242")
        };

        int id = Integer.parseInt(args[0]);
        Transport t = new NettyTransport();
        ThreadContext tc = new SingleThreadContext("proto-%d", new Serializer());
        List<Integer> knownPeers = new ArrayList<>();

        tc.execute(() -> {
            Clique c = new Clique(t, id, addresses);

            c.handler(Integer.class, (j,m)-> {
                System.out.println(id + " recebeu " + m + " de " + j);
                knownPeers.add(m);
            }).onException(e->{
                // exception handler
            });

            c.open().thenRun(() -> {
                for(int i = 0; i < addresses.length; i++)
                    c.send(i, id);

                ThreadContext.currentContext().schedule(Duration.ofSeconds(5), () -> {
                    knownPeers.sort(null);
                    System.out.println("TIMEOUT: the chosen leader is " + knownPeers.get(0));
                });
            });
        }).join();
    }
}
