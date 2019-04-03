package Game.Client.GameStages;

// The class is responsible for establishing connection
// with the server. It is also responsible for placing messages
// into the queue . The idea is to sent connection packet,
// get a response and pass the execution to a new thread
// which is responsible for the

import Game.Main;
import Game.Packets.ConnectionResponsePacket;
import Game.Packets.SquareStringRequest;
import Game.Strategies.ConnectionRequest;
import Game.Strategies.StringRequest;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;

import static Game.Client.Client.outgoingPackets;
import static Game.Main.*;

public class Connection implements Runnable{

  public static ConnectionResponsePacket gameData = null;
  public static Boolean cndVar = false;

  public void run(){

          while (boardInit== false) {
        System.out.println("adding packet to the queue");
        // send requests every 3 seconds until response from server arrives;
            try {
              Thread.sleep(3000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            System.out.println("packet addded to the queueue");
        DatagramPacket packet = new ConnectionRequest().generateClientRequest();
        outgoingPackets.add(packet);
      }
    System.out.println("Board Init is set to true");

    boardHeight = gameData.getBoardHeight();
    boardWidth = gameData.getBoardWidth();
    Main.game.setNumSqInCol(gameData.getNumSqInColumn());
    Main.game.setNumSqInRow(gameData.getNumSqInRow());
    Main.game.setBrushSize(gameData.getBrushSize());

    // initalize grid;
      game.initializeGrid();

      System.out.println("grid initalized");

    // Board Initalization;

      while(true){
          try {
              // create request Packet
              SquareStringRequest ssr = game.requestPakets.peek();
              DatagramPacket packet = new StringRequest().generateClientRequest(ssr);

              // put the datagram packet into the queue
              outgoingPackets.put(packet);

            synchronized (cndVar){
              cndVar.wait();
            }

            } catch(InterruptedException e){
              e.printStackTrace();
            }
          if(game.requestPakets.isEmpty()==true){
            System.out.println("all packets filled");
            break;
          }
      }


      
  }
}
