package Game.Server;

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
      System.out.println(request.getClientPort());

      Server instance = Server.getInstance();
      GameData gameData = instance.getGameData();

      ConnectionResponsePacket response = new ConnectionResponsePacket();
      response.setBrushSize(gameData.getBrushSize());
      response.setBoardWidth(gameData.getBoardWidth());
      response.setBoardHeight(gameData.getBoardHeight());
      response.setNumSqInRow(gameData.getNumSqInRow());
      response.setNumSqInColumn(gameData.getNumSqInCol());

      Serializable r = (Serializable) response;

      byte stamp = (byte) 2;
      System.out.println("Stamp is " + stamp);

      data = convertObjectToBytes(stamp,r);

    } catch(IOException e){e.printStackTrace();}

      DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

      return packet;

    }


}
