package fsd;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncLineBuffer {

    private ByteBuffer buf, line;

    private AsynchronousSocketChannel sock;

    public <A> void read(final A value, CompletionHandler<String, A> handler) {

        while(buf.hasRemaining()) {
            byte c = buf.get();
            if (c == '\n') {
                line.flip();
                String res = new String(line);
                handler.completed(res, value);
                return;
            } else {
                line.put(c);
            }
        }

        buf.clear();
        sock.read(buf, new CompletionHandler<>() {
            @Override
            public void completed(Object o, Object o2) {
                read(value, handler);
            }

            @Override
            public void failed(Throwable throwable, Object o) {

            }
        }});
    }

    private CompletionHandler<String, Object> wHandler;
    private Object wValue;

    public <A> void write(String line, CompletionHandler<Void, A> handler) {
		...
    }
}
