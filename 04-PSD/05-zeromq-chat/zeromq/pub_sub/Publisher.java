package pub_sub;
import org.zeromq.ZMQ;

public class Publisher {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://*:" + args[0]);
        while (true) {
          String s = System.console().readLine();
          if (s == null) break;
          socket.send(s);
        }
        socket.close();
        context.term();
    }
}

