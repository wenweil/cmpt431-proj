package Game.Server;

import Game.Main;
import Game.Packets.ConnectionRequestPacket;
import Game.Packets.ConnectionResponsePacket;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;

import static Game.Utilities.Utilities.convertObjectToBytes;

public class ServerConnectionResponse implements ServerResponseStrategy {

  public DatagramPacket generateServerResponse(InetAddress address, int port, Serializable requestObject){

    byte[] data = new byte[0];

    try {
      ConnectionRequestPacket request = (ConnectionRequestPacket) requestObject;
      Server instance = Server.getInstance();
      GameData gameData = instance.getGameData();
      
      Main.game.Adduser(address,port,request.getUID());

      ConnectionResponsePacket response = new ConnectionResponsePacket();
      response.setBrushSize(gameData.getBrushSize());
      response.setBoardWidth(gameData.getBoardWidth());
      response.setBoardHeight(gameData.getBoardHeight());
      response.setNumSqInRow(gameData.getNumSqInRow());
      response.setNumSqInColumn(gameData.getNumSqInCol());
      response.setThreshold(gameData.getThreshold());
      Serializable r = (Serializable) response;

      byte stamp = (byte) 2;

      data = convertObjectToBytes(stamp,r);

    } catch(IOException e){e.printStackTrace();}

      DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

      return packet;

    }


}
