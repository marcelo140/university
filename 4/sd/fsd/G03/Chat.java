package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Chat {
    private List<Client> clients = new ArrayList<>();
    private synchronized boolean add(Client cli) {
        clients.add(cli);
    }

    private synchronized void broadcast(ByteBuffer buf) {
        for(Client cli: clients) {
            cli.send(buf);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AsynchronousChannelGroup group = AsynchronousChannelGroup
            .withFixedThreadPool(1, Executors.defaultThreadFactory());

        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel
            .open(group)
            .bind(new InetSocketAddress(5000));

        ByteBuffer buf = ByteBuffer.allocate(1024);

        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(AsynchronousSocketChannel ch, Void att) {
                new Client(buf, ch);
                listener.accept(null, this);
            }

            public void failed(Throwable e, Void att) { 
                System.exit(1); 
            }
        });

        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}
