package Game.Server;

import Game.Strategies.stamps;
import Game.Utilities.Utilities;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Listener implements Runnable {

  private Server server;
  private HashMap<String,ServerResponseStrategy> strategies;

  Listener(Server server) throws SocketException {
    this.server = server;
    strategies = new HashMap<>();

    strategies.put("ServerConnectionResponse", new ServerConnectionResponse());

    // expand as new strategies are added;
  }

  public void run(){
    try {

      while (true) {

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // blocks until a packet is received
        DatagramSocket udpSocket = server.getUdpSocket();

        udpSocket.receive(packet);

        processIncomingPacket(packet);

      }

    } catch (IOException|ClassNotFoundException i) {
      i.printStackTrace();
    }
  }


  public void processIncomingPacket(DatagramPacket packet) throws IOException, ClassNotFoundException {
    class ProcessPacket implements Runnable{

      DatagramPacket packet;
      ProcessPacket(DatagramPacket packet){
        this.packet = packet;
      }


      public void run(){
        byte[] packetData = packet.getData();

        InetAddress remoteAddress = packet.getAddress();
        int remotePort = packet.getPort();

        // extract stamp
        byte stamp = packetData[0];

        // extract data size
        int size = Utilities.extractPacketSize(packet);

        // get data
        byte[] data = Utilities.extractData(packet,size);

        try {
          Serializable object = Utilities.deserializeObject(data);

          if (stamp == 1){
            DatagramPacket dp = strategies.get("ServerConnectionResponse").generateServerResponse(remoteAddress,remotePort,object);
            Server.outgoingQueue.put(dp);

          }
          else if (stamp == 3){
            DatagramPacket dp = new ServerStringResponse().generateServerResponse(remoteAddress,remotePort,object);
            Server.outgoingQueue.put(dp);
          }
          else if(stamp == stamps.MUTEXUNLOCKREQUEST.val()){
            DatagramPacket dp = new ServerMutexResponse().generateServerResponse(remoteAddress,remotePort,object);
            Server.outgoingQueue.put(dp);
          }
          else if(stamp == 7){

          }

        }
        catch (IOException |ClassNotFoundException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }


      }
    }

    new Thread(new ProcessPacket(packet)).start();
  }

}
