import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;

import io.atomix.catalyst.serializer.Serializer;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;

import io.atomix.catalyst.transport.netty.NettyTransport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import pt.haslab.ekit.Clique;

public class App {
    private static Clique c;
    private static ThreadContext tc;
    private static HashSet<Integer> connected = new HashSet<>();

    private static void inputRead() throws Exception {
        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()) {
            String msg = in.nextLine();
            tc.execute(() -> {
                for(Integer i: connected)
                    c.send(i, msg);
            });
        }
    }

    public static void main(String[] args) throws Exception
    {
        int id = Integer.parseInt(args[0]);
        Address[] addresses = {
            new Address("127.0.0.1:1240"),
            new Address("127.0.0.1:1241"),
            new Address("127.0.0.1:1242")
        };

        Transport t = new NettyTransport();
        tc = new SingleThreadContext("proto-%d", new Serializer());
        c = new Clique(t, id, addresses);

        tc.execute(() -> {
            c.handler(String.class, (sender, message) -> {
                System.out.println(message);
                connected.add(sender);
            }).onException(e -> {
                System.out.println("Error: " + e.getMessage());
            });

            c.open().thenRun(() -> {
                for(int i = 0; i < addresses.length; i++)
                    if (i != id)
                        c.send(i, "Connected");
            });
        }).join();

        inputRead();
    }
}
