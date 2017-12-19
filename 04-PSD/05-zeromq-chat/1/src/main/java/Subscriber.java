import org.zeromq.ZMQ;

public class Subscriber extends Thread {
    ZMQ.Context context;
    ZMQ.Socket socket;

    public Subscriber(ZMQ.Context context) {
        this.context = context;

        socket = context.socket(ZMQ.SUB);
        socket.connect("tcp://localhost:5000");
    }

    public void run() {
        while(true) {
            byte[] msg = socket.recv();
            System.out.println(new String(msg));
        }
    }
}
