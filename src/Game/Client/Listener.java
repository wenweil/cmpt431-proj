package Game.Client;

import Game.Client.GameStages.Connection;
import Game.Main;
import Game.Packets.ConnectionResponsePacket;
import Game.Packets.SquareStringRequest;
import Game.Packets.SquareStringResponse;
import Game.Strategies.stamps;
import Game.Utilities.Utilities;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static Game.Client.GameStages.Connection.cndVar;
import static Game.Main.boardInit;
import static Game.Main.game;


public class Listener implements Runnable {

  public void run(){

    System.out.println("Listener started");

    // Size of the packet is set to 512 bytes

    byte[] buffer = new byte[1024];
    try {
      while (true) {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // blocks until a packet is received
        DatagramSocket udpSocket = Client.getInstance().getClientSocket();

        udpSocket.receive(packet);

        
        processIncomingPacket(packet);

      }
    }
    catch (IOException|ClassNotFoundException e) {
      e.printStackTrace();
    }
  }



  public void processIncomingPacket(DatagramPacket packet) throws IOException, ClassNotFoundException {
    class ProcessPacket implements Runnable{

      DatagramPacket packet;
      ProcessPacket(DatagramPacket packet){
        this.packet = packet;
      }


      public void run(){
    	  
    	System.out.println("received");
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

          if(stamp == 2){
            if(Connection.gameData == null) {
              Connection.gameData = (ConnectionResponsePacket) object;
              boardInit = true;
            }
          }
          else if(stamp == 4){

            SquareStringResponse ssr = (SquareStringResponse) object;
            String squareCode = ssr.getCode();

            int col = ssr.getColNumber();
            int row = ssr.getRowNumber();

            synchronized (cndVar){
              cndVar.notify();
            }

            // create object and make sure if it exists in the queue;
            SquareStringRequest ssrsq = new SquareStringRequest(col,row);

            //System.out.println(game.requestPakets.size());

              if (Main.game.requestPakets.contains(ssrsq) == true) {
                System.out.println("object removed from the queue");
                Main.game.requestPakets.remove(ssrsq);
                game.addSquare(ssr.getCode(), col, row);
              }

              System.out.println(game.getSquare().size());

            }
          else if (stamp == stamps.MUTEXLOCKRESPONSE.val()) {
        	  
          }

        }
        catch (IOException |ClassNotFoundException e) {
          e.printStackTrace();
        }


      }
    }

    new Thread(new ProcessPacket(packet)).start();
  }
}