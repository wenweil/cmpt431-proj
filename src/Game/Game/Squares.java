package Game.Game;

import Game.Client.Client;
import Game.Packets.MutexRequestPacket;
import Game.Utilities.Utilities;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Squares {

  private static Squares squares = new Squares();

  public static Squares getInstance(){return squares;}



  private String currentSquareID;
  private Boolean hasLock = false;
  private Boolean message = false;

  public Boolean getMessage() {
    return message;
  }

  public void setMessage(Boolean message) {
    this.message = message;
  }




  /*Callable<Boolean> requestMutex = () -> {

    //Serializable mutexRequest = new MutexRequestPacket(currentSquareID);

    byte[] packetData = Utilities.convertObjectToBytes((byte) 5,mutexRequest);
    InetAddress address = Client.getInstance().getServerAddress();
    int port = Client.getInstance().getServerPort();

    DatagramPacket packet = new DatagramPacket(packetData,packetData.length,address,port);



    synchronized (hasLock){
      while(message==false){
        Client.outgoingPackets.put(packet);
        message.wait(1000);
      }
    }

    message = false;

    return hasLock;
  };*/

  Callable<String> releaseMutex = () -> {

    // Generate Datagram Packet
    // Place Datagram Packet in the outgoing queue

    // go to sleep
    // if packet is recieved wake up from the other thread;

    return "Released";
  };


  /*Future<Boolean> requestMutex(){
    return Client.getInstance().getThreadExecutor().submit(requestMutex);
  }

  Future<String> releaseMutex(){
    return Client.getInstance().getThreadExecutor().submit(releaseMutex);
  }*/



  public String getCurrentSquareID() {
    return currentSquareID;
  }

  public void setCurrentSquareID(String currentSquareID) {
    this.currentSquareID = currentSquareID;
  }

  public Boolean getHasLock() {
    return hasLock;
  }

  public void setHasLock(Boolean hasLock) {
    this.hasLock = hasLock;
  }



 /* public Callable<Boolean> getRequestMutex() {
    return requestMutex;
  }

  public void setRequestMutex(Callable<Boolean> requestMutex) {
    this.requestMutex = requestMutex;
  }*/



}
