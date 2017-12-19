package ex3;

import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.haslab.ekit.Clique;

public class Elector {
    private int id;
    private int leader;
    private int friends;

    Elector(int id, int friends) {
        this.id = id;
        this.leader = id;
        this.friends = friends;
    }

    public static void main(String[] args) throws InterruptedException {
        Address[] addresses = {
            new Address("127.0.0.1:1140"),
            new Address("127.0.0.1:1141"),
            new Address("127.0.0.1:1142")
        };
        Transport t = new NettyTransport();
        Elector e = new Elector(Integer.parseInt(args[0]), addresses.length - 1);

        ThreadContext tc = new SingleThreadContext("tc-%d", new Serializer());

        tc.execute(() -> {
            Clique c = new Clique(t, e.id, addresses);

            c.handler(Integer.class, (sender, pid) -> {
                e.friends--;
                System.out.println(sender + " wants to be the leader.");

                if (pid < e.leader)
                    e.leader = pid;

                if (e.friends == 0)
                    System.out.printf("Process %d: My leader is %d\n", e.id, e.leader);
            });

            c.open().thenRun(() -> {
                for(int i = 0; i < addresses.length; i++) {
                    c.send(i, e.id);
                }
            });
        }).join();
    }
}
