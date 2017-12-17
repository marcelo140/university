package bot;

import java.net.Socket;
import java.io.IOException;

public class Bot {
    public static void main(String[] args) throws IOException {
        long writeDelay = Long.parseLong(args[0]);
        long readDelay = Long.parseLong(args[1]);

        Socket server = new Socket("localhost", 5000);

        Reader reader = new Reader(server, readDelay);
        Writer writer = new Writer(server, writeDelay);

        reader.start();
        writer.start();
    }
}