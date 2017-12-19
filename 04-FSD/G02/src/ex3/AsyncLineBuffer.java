package ex3;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncLineBuffer {
    private AsynchronousSocketChannel sock;
    private ByteBuffer buf, line;

    AsyncLineBuffer(AsynchronousSocketChannel sock) {
        this.sock = sock;

        buf = ByteBuffer.allocate(1024).flip();
        line = ByteBuffer.allocate(1024);
    }

    public <A> void read(final A value, CompletionHandler<String, A> handler) {
        while(buf.hasRemaining()) {
            byte ch = buf.get();

            if (ch == '\n') {
                line.flip();
                byte[] ba = new byte[line.limit()];
                line.get(ba, 0, line.limit());

                line.clear();
                String strLine = new String(ba);
                handler.completed(strLine, value);

                return;
            } else {
                line.put(ch);
            }
        }

        buf.clear();
        sock.read(buf, null, new CompletionHandler<Integer, A>() {
            public void completed(Integer result, A attachment) {
                buf.flip();
                read(value, handler);
            }

            public void failed(Throwable exc, A attachment) {
                exc.printStackTrace();
            }
        });

    }

    public <A> void write(String line, final A value, CompletionHandler<Void, A> handler) {
        ByteBuffer out = ByteBuffer.allocate(line.length() + 1)
            .put(line.getBytes())
            .put((byte)'\n')
            .flip();

        sock.write(out, null, new CompletionHandler<Integer, Void>() {
            public void completed(Integer result, Void attachment) {
                if (out.hasRemaining()) {
                    out.remaining();
                    String strRemaining = new String(out.array());
                    write(strRemaining, value, handler);
                } else {
                    handler.completed(null, value);
                }
            }

            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });
    }
}
