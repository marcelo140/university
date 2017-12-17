package bot;

import java.lang.Thread;
import java.net.Socket;
import java.io.*;

public class Reader extends Thread {
    private Socket server;
    private long sleepTime;

    Reader(Socket server, long sleepTime) {
        this.server = server;
        this.sleepTime = sleepTime;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(server.getInputStream()));
            int counter = 0;
            long tStart = System.currentTimeMillis();

            while(true) {
                counter++;
                in.readLine();
                sleep(sleepTime);    

                if (counter == 1000) {
                    long tEnd = System.currentTimeMillis();
                    System.out.println("1000 messages read: " + (tEnd - tStart) / 1000.0);
                    counter = 0;
                    tStart = System.currentTimeMillis();
                }
            }

        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
