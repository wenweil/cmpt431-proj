package Game.Game;

import Game.Client.Client;
import Game.Packets.MutexReleasePacket;
import Game.Packets.MutexRequestPacket;
import Game.Utilities.Utilities;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class SquareNetworkHandler {

  private static ActiveSquare activeSquare;

  // shared variable for synchronization;
  // turns true when message from server is arrived;
  public static Boolean message = false;


  // information from the server;
  public static Boolean hasLock = false;



  public static ActiveSquare getActiveSquare(){
    if(activeSquare == null){
      activeSquare = new ActiveSquare();
      return activeSquare;
    }
    else{return activeSquare;}

  }

  public static void deleteActiveSquare(){
    activeSquare=null;
  }





}
