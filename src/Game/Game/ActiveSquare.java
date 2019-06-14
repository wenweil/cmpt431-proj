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

import static Game.Game.SquareNetworkHandler.hasLock;
import static Game.Game.SquareNetworkHandler.message;

public class ActiveSquare {

  private String currentSquareID;

  public int getGameState() {
    return gameState;
  }


  private int gameState;


  public void setGameState(int gameState) {
    this.gameState = gameState;
  }


  protected ActiveSquare(){ }


  Future<Boolean> requestMutex(){
    return Client.getInstance().getThreadExecutor().submit(requestMutex);
  }

  Future<Boolean> releaseMutex(){
    return Client.getInstance().getThreadExecutor().submit(releaseMutex);
  }



  public String getCurrentSquareID() {
    return currentSquareID;
  }

  public void setCurrentSquareID(String currentSquareID) {
    this.currentSquareID = currentSquareID;
  }



  public Callable<Boolean> getRequestMutex() {
    return requestMutex;
  }

  public void setRequestMutex(Callable<Boolean> requestMutex) {
    this.requestMutex = requestMutex;
  }




  /////////////////////////////////////////////////////////////////////////////////

  Callable<Boolean> requestMutex = () -> {
    System.out.println("CURRENT SQUARE ID: "+ currentSquareID);

    Serializable mutexRequest = new MutexRequestPacket(currentSquareID);

    System.out.println("CURRENT SQUARE ID IS : " + ((MutexRequestPacket) mutexRequest).getSquareCode());

    byte[] packetData = Utilities.convertObjectToBytes((byte) 5,mutexRequest);
    InetAddress address = Client.getInstance().getServerAddress();
    int port = Client.getInstance().getServerPort();

    DatagramPacket packet = new DatagramPacket(packetData,packetData.length,address,port);



    synchronized (message){
      while(message==false){
        Client.outgoingPackets.put(packet);
        message.wait(1000);
      }
    }
    System.out.println("message recieved, mutex acquired");

    message = false;

    return hasLock;
  };


  Callable<Boolean> releaseMutex = () -> {

    System.out.println("release mutex is called ");

    Serializable mutexRelease = new MutexReleasePacket(gameState,currentSquareID);

    byte[] packetData = Utilities.convertObjectToBytes((byte) 7,mutexRelease);
    InetAddress address = Client.getInstance().getServerAddress();
    int port = Client.getInstance().getServerPort();

    DatagramPacket packet = new DatagramPacket(packetData,packetData.length,address,port);

    synchronized (message){
      while(message==false){
        System.out.println("message to server is sent");
        Client.outgoingPackets.put(packet);
        message.wait(1000);
      }
    }

    message = false;

    System.out.println("Lock released");
    return true;
  };

}
