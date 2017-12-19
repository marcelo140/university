package pub_sub;
import org.zeromq.ZMQ;

public class MultipleConnectSub {
  public static void main(String[] args) throws InterruptedException {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket socket = context.socket(ZMQ.SUB);
    for (int i = 2; i < args.length; i++) {
      socket.subscribe(args[i].getBytes());
    }
    socket.connect("tcp://localhost:"+args[0]);
    socket.connect("tcp://localhost:"+args[1]);
    while (true) {
      byte[] b = socket.recv();
      System.out.println(new String(b));
    }
    //socket.close();
    //context.term();
  }
}

