package Game.Server;

import java.io.IOException;
import java.net.DatagramPacket;

public class Sender implements Runnable {
  Server server;

  public Sender(Server server){
    this.server=server;
  }

  public void run(){

    while(true){
      try {
        DatagramPacket packet = Server.outgoingQueue.take();
        System.out.println("packet to client send");
        System.out.println(packet.getAddress()+" "+packet.getPort());
        if(packet != null)
          Server.getInstance().getUdpSocket().send(packet);
        else
          System.out.println("There wes a IOException");

      } catch (InterruptedException| IOException e) {
        e.printStackTrace();
      }

    }

  }
}
