package Game.Server;

import Game.Game.Game;
import Game.Game.Square;
import Game.Packets.MutexReleasePacket;
import Game.Packets.MutexReleaseResponse;
import Game.Utilities.Utilities;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;

public class ServerMutexReleaseResponse implements ServerResponseStrategy  {

  public DatagramPacket generateServerResponse(InetAddress address, int port, Serializable requestObject) throws IOException {

    // cast the object
    MutexReleasePacket releasePacket = (MutexReleasePacket) requestObject;

    // extract square id;
    String squareId = releasePacket.getSquareId();

    // get square state;
    int gameState = releasePacket.getGameState();

    // get mutexes and squares
    HashMap<String, Mutex> mutexes = Server.getInstance().getGameData().getMutexes();
    HashMap<String,SquareStatus> squareStatus = Server.getInstance().getGameData().getSquareStatus();

    // release mutex;
    boolean lockStatus = mutexes.get(squareId).reqUnlock(address.toString());

    System.out.println("lock is released back nto the pool, the value of the lock is " + lockStatus);




    // square is claimed;
    if(gameState == 2){
      squareStatus.put(squareId,SquareStatus.CLAIMED);
    }
    // square is idle;
    else if(gameState == 3){
      squareStatus.put(squareId,SquareStatus.AVAILABLE);
    }

    MutexReleaseResponse mutexReleaseResponse = new MutexReleaseResponse(lockStatus);

    byte data[] = Utilities.convertObjectToBytes((byte) 8, mutexReleaseResponse);

    DatagramPacket newPacket = new DatagramPacket(data,data.length,address,port);

    return newPacket;


  }


}
