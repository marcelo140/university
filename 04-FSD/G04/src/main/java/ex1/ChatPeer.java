package ex1;

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

    private static void read() {
        CompletableFuture
            .supplyAsync(in::nextLine)
            .thenApply(ChatPeer::send)
            .thenRun(ChatPeer::read);
    }

    private static CompletableFuture<Void> send(String msg) {
        CompletableFuture<Void> r = new CompletableFuture<>();

        tc.execute(() -> {
            for (int i = 0; i < peers; i++) {
                if (i != id)
                    c.send(i, msg);
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

        tc.execute(() -> {
            c.handler(String.class, (sender, msg) -> {
                System.out.println(msg);
            });

            c.open().thenRun(ChatPeer::read);
        });
    }
}
