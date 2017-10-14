import java.util.*;
import java.nio.channels.*;
import java.util.concurrent.*;  
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        AsynchronousChannelGroup group = AsynchronousChannelGroup
            .withFixedThreadPool(1, Executors.defaultThreadFactory());

        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel
            .open(group)
            .bind(new InetSocketAddress(5000));

        List<AsynchronousSocketChannel> clients = new ArrayList<>();
        ByteBuffer buf = ByteBuffer.allocate(1024);

        CompletionHandler<AsynchronousSocketChannel, Void> acceptHandler;
        CompletionHandler<Integer, AsynchronousSocketChannel> readHandler, writeHandler;

        writeHandler = new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            public void completed(Integer bytesRead, AsynchronousSocketChannel ch) {
            }

            public void failed(Throwable e, AsynchronousSocketChannel ch) {
            }
        };

        readHandler = new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            public void completed(Integer bytesRead, AsynchronousSocketChannel ch) {
                buf.flip();

                for(AsynchronousSocketChannel peerChannel: clients) {
                    peerChannel.write(buf, ch, null);
                    buf.rewind();
                }

                buf.clear();
                ch.read(buf, ch, this);
            }

            public void failed(Throwable e, AsynchronousSocketChannel ch) {
            }
        };

        acceptHandler = new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(AsynchronousSocketChannel ch, Void att) {
                listener.accept(null, this);

                clients.add(ch);
                ch.read(buf, ch, readHandler);
            }

            public void failed(Throwable e, Void att) {
            }
        };

        listener.accept(null, acceptHandler);
        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}
