package ex3;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.Queue;

public class ChatClient {
    private AsyncLineBuffer buf;
    private Chat chat;
    private Queue<String> out = new LinkedList<>();

    ChatClient(AsynchronousSocketChannel ch, Chat chat) {
        this.buf = new AsyncLineBuffer(ch);
        this.chat = chat;

        // When a client connects, start reading from the socket immediately
        buf.read(null, rch);
    }

    private CompletionHandler<Void, Void> wch = new CompletionHandler<>() {
        public void completed(Void v1, Void v2) {
            synchronized (out) {
                out.remove();
                String msg = out.peek();
                if (msg != null) {
                    buf.write(msg, null, wch);
                }
            }
        }

        public void failed(Throwable e, Void v) {
        }
    };

    public void send(String s) {
        synchronized (out) {
            boolean wasEmpty = out.isEmpty();

            out.add(s);
            if (wasEmpty) {
                buf.write(s, null, wch);
            }
        }
    }

    private CompletionHandler<String, Void> rch = new CompletionHandler<>() {
        public void completed(String str, Void v) {
            chat.send(str);

            buf.read(null, this);
        }

        public void failed(Throwable t, Void v) {
        }
    };
}
