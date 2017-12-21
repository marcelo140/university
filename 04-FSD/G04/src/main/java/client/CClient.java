package client;

import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.netty.NettyTransport;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class CClient {
    private static ThreadContext tc = new SingleThreadContext("tc-%d", new Serializer());
    private static Scanner in = new Scanner(System.in);
    private static Connection conn;

    public static void read() {
        CompletableFuture
            .supplyAsync(in::nextLine)
            .thenAccept(CClient::send)
            .thenRun(CClient::read);
    }

    private static void send(String msg) {
        tc.execute(() -> {
            conn.send(msg);
        });
    }

    public static void main(String[] args) {
        NettyTransport t = new NettyTransport();

        tc.execute(() -> {
            t.client().connect(new Address(args[0], Integer.parseInt(args[1])))
                .thenAccept(connection -> {
                    conn = connection;
                    System.out.println("I'm connected");
                    conn.handler(String.class, str -> {
                        System.out.println(str);
                    });
                    read();
                });
        });
    }
}
