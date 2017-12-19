package ex2;

import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.haslab.ekit.Clique;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Elector {
    private int id;
    private int leader;

    Elector(int id) {
        this.id = id;
        this.leader = id;
    }

    public static void main(String[] args) throws InterruptedException {
        Elector e = new Elector(Integer.parseInt(args[0]));
        Transport t = new NettyTransport();
        Address[] addresses = {
            new Address("127.0.0.1:1140"),
            new Address("127.0.0.1:1141"),
            new Address("127.0.0.1:1142")
        };

        ThreadContext tc = new SingleThreadContext("tc-%d", new Serializer());

        tc.execute(() -> {
            Clique c = new Clique(t, e.id, addresses);

            c.handler(Integer.class, (sender, pid) -> {
                System.out.println(sender + " wants to be the leader.");
                if (pid < e.leader)
                    e.leader = pid;
            });

            c.open().thenRun(() -> {
                for(int i = 0; i < addresses.length; i++) {
                    c.send(i, e.id);
                }

                CompletableFuture
                    .runAsync( () -> {
                        try {
                            TimeUnit.SECONDS.sleep(10);
                        } catch(Exception exc) {
                            System.out.println("Can't wait. I'm too excited");
                        }
                    }).thenRun(() ->
                        System.out.printf("Process %d: My leader is %d\n", e.id, e.leader)
                    );
            });
        }).join();
    }
}
