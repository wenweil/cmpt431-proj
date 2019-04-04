package Game.Client;

import Game.Client.GameStages.Connection;
import Game.Game.Square;
import Game.Game.Squares;
import Game.Main;
import Game.Packets.ClientDrawingDataPacket;
import Game.Packets.ConnectionResponsePacket;
import Game.Packets.MutexResponsePacket;
import Game.Packets.SendStringPacket;
import Game.Packets.SquareStringRequest;
import Game.Packets.SquareStringResponse;
import Game.Strategies.stamps;
import Game.Utilities.Utilities;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import static Game.Client.GameStages.Connection.cndVar;
import static Game.Client.GameStages.Connection.currentRequest;
import static Game.Game.Game.reqLock;
import static Game.Main.boardInit;


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

            int col = ssr.getColNumber();
            int row = ssr.getRowNumber();

             SquareStringRequest ssrsq = new SquareStringRequest(col,row);

            if(ssrsq.equals(Connection.currentRequest)){
             Main.squareResponses.add(ssr);
              synchronized (cndVar){
                currentRequest=null;
                cndVar.notify();
              }
            }
          }
          else if (stamp == 6){
            // mutex request response block
            MutexResponsePacket mutres = (MutexResponsePacket) object;
            Boolean lock = mutres.getLock();

            synchronized (reqLock){
              reqLock = lock;
              reqLock.notify();
            }
          }
          else if (stamp == stamps.DRAWINGDATA.val()) {
        	  ClientDrawingDataPacket p = (ClientDrawingDataPacket) object;
        	  HashMap<String,Square> squares = Main.game.getSquare();
        	  squares.get(p.getEID()).drawline(p.getX1(), p.getY1(), p.getX2(), p.getY2(), p.getColor());
          }else if (stamp == stamps.DRAWFAIL.val()) {
        	  SendStringPacket p = (SendStringPacket) object;
        	  HashMap<String,Square> squares = Main.game.getSquare();
        	  squares.get(p.getString()).clear();
        	  
          }else if(stamp == stamps.DRAWCLAIM.val()) {
        	  SendStringPacket p = (SendStringPacket) object;
        	  HashMap<String,Square> squares = Main.game.getSquare();
        	  String eid = p.getString().split(";")[0];
        	  String color = p.getString().split(";")[1];
        	  squares.get(eid).fill(Color.valueOf(color));;
          }

        }
        catch (IOException |ClassNotFoundException e) {
          e.printStackTrace();
        }


      }
    }

    Thread task = new Thread(new ProcessPacket(packet));

    Client.getInstance().getThreadExecutor().submit(task);
  }
}