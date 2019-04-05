package Game.Server;

import Game.Game.Square;
import Game.Packets.MutexRequestPacket;
import Game.Packets.MutexResponsePacket;
import Game.Utilities.Utilities;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;


public class ServerMutexResponse implements ServerResponseStrategy {
  public DatagramPacket generateServerResponse(InetAddress address, int port, Serializable requestObject) throws IOException {

    MutexRequestPacket mrp = (MutexRequestPacket) requestObject;

    String squareID = mrp.getSquareCode();

    // find square code in the hashmap
    HashMap<String,Mutex> mutexes = Server.getInstance().getGameData().getMutexes();

    // find square status in the hashmap;
    HashMap<String, SquareStatus> sqstatus = Server.getInstance().getGameData().getSquareStatus();
    SquareStatus status = sqstatus.get(squareID);
    System.out.println(status);


    Mutex mutex = mutexes.get(squareID);
    

    Boolean val;
    if(status==SquareStatus.AVAILABLE){
      sqstatus.put(squareID,SquareStatus.BEING_COLORED);
      val = mutex.reqLock(address.toString());
      System.out.println("Value of the mutex is "+ val);
    }
    else{
      val = false;
    }

    MutexResponsePacket response = new MutexResponsePacket();

    if(mutexes.containsKey(squareID)){
      response.setLock(val);
      response.setSquareCode(squareID);
    }
    else{
      response.setLock(false);
      response.setSquareCode(squareID);
    }

    byte[] data = Utilities.convertObjectToBytes((byte) 6, response);
    DatagramPacket packet = new DatagramPacket(data,data.length,address,port);

    return packet;

  }
}
