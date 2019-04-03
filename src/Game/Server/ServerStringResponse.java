package Game.Server;

import Game.Packets.SquareStringRequest;
import Game.Packets.SquareStringResponse;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;

import static Game.Utilities.Utilities.convertObjectToBytes;

public class ServerStringResponse implements ServerResponseStrategy {
  public DatagramPacket generateServerResponse(InetAddress destAddress, int port, Serializable object) throws IOException {

    SquareStringRequest ssrq = (SquareStringRequest) object;
    int row = ssrq.getRowNumber();
    int col = ssrq.getColNumber();

    String cellCode = Server.getInstance().gameData.getSquareCode(row,col);

    SquareStringResponse ssrs = new SquareStringResponse(col,row,cellCode);

    byte stamp = (byte) 4;
    byte[] data = convertObjectToBytes(stamp,ssrs);

    DatagramPacket packet = new DatagramPacket(data, data.length, destAddress, port);

    return packet;
  }

}
