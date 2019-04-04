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

import java.net.DatagramPacket;

import static Game.Client.Client.outgoingPackets;
import static Game.Main.*;

public class Connection implements Runnable{

  public static ConnectionResponsePacket gameData = null;
  public static SquareStringRequest currentRequest = null;
  public static Boolean cndVar = false;

  public void run(){

    while (boardInit== false) {
      System.out.println(Thread.currentThread().getName()+" adding packet to the queue");
        // send requests every 3 seconds until response from server arrives;
      try { Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
      DatagramPacket packet = new ConnectionRequest().generateClientRequest();
      // add packet to the outgoing queu
      outgoingPackets.add(packet);
      }
    System.out.println(Thread.currentThread().getName()+" Board Init is set to true");

    boardHeight = gameData.getBoardHeight();
    boardWidth = gameData.getBoardWidth();
    Main.game.setNumSqInCol(gameData.getNumSqInColumn());
    
    Main.game.setNumSqInRow(gameData.getNumSqInRow());
    Main.game.setBrushSize(gameData.getBrushSize());
    Main.game.setThreashhold(gameData.getThreshold());

    // initalize grid;
    try {
      game.populateWIthRequestData();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println(Thread.currentThread().getName()+" grid initalized");

    // This segment intializes board squares.
    // requests with the specific board cooardinates are send to server;

      while(true){
          try {
              // create request Packet
              if(game.requestPakets.isEmpty() == true){
            	
                break;
              }
              currentRequest = game.requestPakets.take();
              DatagramPacket packet = new StringRequest().generateClientRequest(currentRequest);

              while(currentRequest != null){
                outgoingPackets.put(packet);

                synchronized (cndVar){
                  cndVar.wait(200);
                }
              }
            } catch(InterruptedException e){
              e.printStackTrace();
            }

          if(game.requestPakets.isEmpty()==true){
            break;
          }
      }

    isReady=true;
    synchronized (isReady){
      System.out.println(Thread.currentThread().getName()+" calling notify all");
      isReady.notify();
      System.out.println(Thread.currentThread().getName()+" notify call called");
    }



  }
}