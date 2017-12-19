package ex4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Chat {
    private List<ChatClient> clients = new ArrayList<>();
    private Map<String, String> accounts = new HashMap<>();

    public synchronized boolean register(String username, String password) {
        if (accounts.containsKey(username))
            return false;

        accounts.put(username,password);
        return true;
    }

    public synchronized boolean login(String username, String password) {
        String pass = accounts.get(username);

        if (pass == null || !pass.equals(password))
            return false;

        return true;
    }

    public synchronized void send(String s) {
        for(ChatClient client: clients) {
            client.send(s);
        }
    }

    private synchronized void newClient(AsynchronousSocketChannel ch) {
        ChatClient client = new ChatClient(ch, this);
        clients.add(client);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AsynchronousChannelGroup group = AsynchronousChannelGroup
            .withFixedThreadPool(5, Executors.defaultThreadFactory());

        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel
            .open(group)
            .bind(new InetSocketAddress(5000));

        final Chat chat = new Chat();

        listener.accept(null, new CompletionHandler<>() {
            public void completed(AsynchronousSocketChannel ch, Object o) {
                listener.accept(null, this);
                chat.newClient(ch);
            }

            public void failed(Throwable e, Object o) {
            }
        });

        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}
