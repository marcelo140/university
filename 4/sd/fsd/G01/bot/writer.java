import java.lang.Thread;
import java.net.Socket;
import java.io.PrintWriter;

class Writer extends Thread {
    private Socket server;
    private long sleepTime;

    Writer(Socket server, long sleepTime) {
        this.server = server;
        this.sleepTime = sleepTime;
    }

    public void run() {
        try {
            PrintWriter writer = new PrintWriter(server.getOutputStream());
            int counter = 0;
            long tStart = System.currentTimeMillis();

            while(true) {
                counter++;
                writer.println("Fun!");
                sleep(sleepTime);    

                if (counter == 1000) {
                    long tEnd = System.currentTimeMillis();
                    System.out.println("1000 messages sent: " + (tEnd - tStart) / 1000.0);
                    counter = 0;
                    tStart = System.currentTimeMillis();
                }
            }

        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
