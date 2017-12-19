import org.zeromq.ZMQ;

public class Publisher extends Thread {
    ZMQ.Context context;
    ZMQ.Socket socket;

    public Publisher(ZMQ.Context context, String port) {
        this.context = context;

        socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://*:" + port);
    }

    public void run() {
        while(true) {
            String str = System.console().readLine();
            if (str == null)
                break;

            socket.send(str);
        }

        socket.close();
    }
}
