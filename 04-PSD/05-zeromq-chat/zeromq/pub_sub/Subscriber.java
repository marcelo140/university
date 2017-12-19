package pub_sub;
import org.zeromq.ZMQ;

public class Subscriber {
  public static void main(String[] args) {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket socket = context.socket(ZMQ.SUB);
    socket.connect("tcp://localhost:"+args[0]);
    if (args.length == 1)
      socket.subscribe("".getBytes());
    else for (int i = 1; i < args.length; i++)
      socket.subscribe(args[i].getBytes());
    while (true) {
      byte[] b = socket.recv();
      System.out.println(new String(b));
    }
    //socket.close();
    //context.term();
  }
}

