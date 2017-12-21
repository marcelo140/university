package ex2;

import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.haslab.ekit.Clique;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class ChatPeer {
    private static Scanner in = new Scanner(System.in);
    private static int id, peers;
    private static Clique c;
    private static ThreadContext tc = new SingleThreadContext("tc-%d", new Serializer());
    private static Clock clock;
    private static PackageQueue queue;

    private static void read() {
        CompletableFuture
            .supplyAsync(in::nextLine)
            .thenApply(ChatPeer::send)
            .thenRun(ChatPeer::read);
    }

    private static CompletableFuture<Void> send(String msg) {
        CompletableFuture<Void> r = new CompletableFuture<>();

        clock.increment();
        System.out.println(clock);
        Package pkg = new Package(msg, clock);

        tc.execute(() -> {
            for (int i = 0; i < peers; i++) {
                if (i != id)
                    c.send(i, pkg);
            }

            r.complete(null);
        });

        return r;
    }

    public static void main(String[] args) {
        Transport t = new NettyTransport();
        Address[] addresses = {
                new Address("127.0.0.1:1140"),
                new Address("127.0.0.1:1141"),
                new Address("127.0.0.1:1142")
        };

        id = Integer.parseInt(args[0]);
        peers = addresses.length;
        c = new Clique(t, id, addresses);
        clock = new Clock(id, peers);
        queue = new PackageQueue(peers);

        tc.serializer().register(Clock.class);
        tc.serializer().register(Package.class);

        tc.execute(() -> {
            c.handler(Package.class, (sender, pkg) -> {
                clock.increment();
                clock.update(pkg.getClock());
                System.out.println(clock);

                queue.add(sender, pkg);

                while(queue.hasReady(clock)) {
                    String message = queue.getReady(clock).getMessage();
                    System.out.println(message);
                }
            });

            c.open().thenRun(ChatPeer::read);
        });
    }
}
