package chat;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Queue;

public class Client {
    private final AsynchronousSocketChannel socket;
    private final Chat chat;
    private ByteBuffer in;
    private Queue<ByteBuffer> out;

    Client(Chat chat, AsynchronousSocketChannel socket, ByteBuffer buf) {
        this.chat = chat;
        this.buf = buf;
        this.socket = socket;

        chat.add(this);
        socket.read(buf, this, rHandler);
    }

    public void send(ByteBuffer buf) {
        out.push(buf);

        if (out.size() == 1) {
            socket.write(buf, null, wHandler);
        }
    }

    private static CompletionHandler wHandler = new CompletionHandler<Integer, Client>() {
        public void completed(Integer bytesRead, Client client) {
        }

        public void failed(Throwable e, Client ch) {
        }
    };

    private static CompletionHandler rHandler = new CompletionHandler<Integer, Client>() {
        public void completed(Integer bytesRead, Client cli) {
            cli.buf.flip();

            cli.buf.clear();
            cli.ch.read(cli.buf, cli, rHandler);
        }

        public void failed(Throwable e, Client ch) {
        }
    };
}
