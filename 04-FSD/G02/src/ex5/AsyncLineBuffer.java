package ex5;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

public class AsyncLineBuffer {
    private AsynchronousSocketChannel sock;
    private ByteBuffer buf, line;

    AsyncLineBuffer(AsynchronousSocketChannel sock) {
        this.sock = sock;

        buf = ByteBuffer.allocate(1024).flip();
        line = ByteBuffer.allocate(1024);
    }

    public CompletableFuture<String> read() {
        return readRecursive(new CompletableFuture<>());
    }

    private CompletableFuture<String> readRecursive(CompletableFuture<String> comp) {
        while(buf.hasRemaining()) {
            byte ch = buf.get();

            if (ch == '\n') {
                line.flip();

                byte[] ba = new byte[line.limit()];
                line.get(ba, 0, line.limit());
                String strLine = new String(ba);

                line.clear();
                comp.complete(strLine);
                return comp;
            } else {
                line.put(ch);
            }
        }

        buf.clear();
        sock.read(buf, null, new CompletionHandler<Integer, Void>() {
            public void completed(Integer result, Void attachment) {
                buf.flip();
                readRecursive(comp);
            }

            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });

        return comp;
    }

    public CompletableFuture<Void> write(String line) {
        return writeRecursive(line, new CompletableFuture<>());
    }

    private CompletableFuture<Void> writeRecursive(String line, CompletableFuture<Void> comp) {
        ByteBuffer out = ByteBuffer.allocate(line.length() + 1)
            .put(line.getBytes())
            .put((byte)'\n')
            .flip();

        sock.write(out, null, new CompletionHandler<Integer, Void>() {
            public void completed(Integer result, Void attachment) {
                if (out.hasRemaining()) {
                    out.remaining();
                    String strRemaining = new String(out.array());
                    writeRecursive(strRemaining, comp);
                } else {
                    comp.complete(null);
                }
            }

            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });

        return comp;
    }
}
