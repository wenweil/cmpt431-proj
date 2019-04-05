package Game.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MutexReleasePacket implements Serializable {


  public MutexReleasePacket(int gameState, String squareId) {
    this.gameState = gameState;
    this.squareId = squareId;
  }

  public int getGameState() {
    return gameState;
  }

  public void setGameState(int gameState) {
    this.gameState = gameState;
  }

  public String getSquareId() {
    return squareId;
  }

  public void setSquareId(String squareId) {
    this.squareId = squareId;
  }

  private int gameState;
  private String squareId;


  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
  }


}
