package ex1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.Queue;

public class ChatClient {
    private AsynchronousSocketChannel ch;
    private Chat chat;
    // NOTE: THIS IS AN UNBOUNDED QUEUE!!! Your JVM memory will explode if you
    // spam it enough :)
    private Queue<ByteBuffer> out = new LinkedList<>();
    private ByteBuffer in = ByteBuffer.allocate(1024);

    ChatClient(AsynchronousSocketChannel ch, Chat chat) {
        this.ch = ch;
        this.chat = chat;

        // When a client connects, start reading from the socket immediately
        ch.read(in, null, rch);
    }

    private CompletionHandler<Integer, Object> wch = new CompletionHandler<>() {
        public void completed(Integer bytesRead, Object o) {
            synchronized (out) {
                out.remove();
                ByteBuffer msg = out.peek();
                if (msg != null) {
                    ch.write(msg, null, wch);
                }
            }
        }

        public void failed(Throwable e, Object o) {
        }
    };

    public void send(ByteBuffer msg) {
        synchronized (out) {
            boolean wasEmpty = out.isEmpty();

            out.add(msg);
            if (wasEmpty) {
                ch.write(msg, null, wch);
            }
        }
    }

    private CompletionHandler<Integer, Object> rch = new CompletionHandler<>() {
        public void completed(Integer bytesRead, Object o) {
            if (bytesRead < 0) {
                try {
                    ch.close();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            in.flip();
            chat.send(in);

            // To avoid allocating I would need a pool of Bytebuffers and reuse
            // them once they are free.  It's to much of an hassle so I won't do it.
            in = ByteBuffer.allocate(1024);
            ch.read(in, ch, this);
        }

        public void failed(Throwable t, Object ch) {
        }
    };
}
