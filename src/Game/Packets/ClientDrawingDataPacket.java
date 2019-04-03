package Game.Packets;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// Class ClientDrawingDataPacket.
// Contains the information which is used to draw
public class ClientDrawingDataPacket implements Serializable {

  private ClientDrawingDataPacket() {}

  private Double x1;
  private Double y1;
  private Double x2;
  private Double y2;
  private transient Color color;

  private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    Double red = in.readDouble();
    Double green = in.readDouble();
    Double blue = in.readDouble();
    Double opacity = in.readDouble();
    color = new Color(red,green,blue,opacity);

  }

  private void writeObject(ObjectOutputStream out) throws IOException{
    out.defaultWriteObject();
    out.writeDouble(color.getRed());
    out.writeDouble(color.getGreen());
    out.writeDouble(color.getBlue());
    out.writeDouble(color.getOpacity());

  }

  public Double getX1() {
    return x1;
  }

  public void setX1(Double x1) {
    this.x1 = x1;
  }

  public Double getY1() {
    return y1;
  }

  public void setY1(Double y1) {
    this.y1 = y1;
  }

  public Double getX2() {
    return x2;
  }

  public void setX2(Double x2) {
    this.x2 = x2;
  }

  public Double getY2() {
    return y2;
  }

  public void setY2(Double y2) {
    this.y2 = y2;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }



  public static class PacketBuilder {
    private Double x1;
    private Double y1;
    private Double x2;
    private Double y2;
    private transient Color color;

    public ClientDrawingDataPacket build(){
      ClientDrawingDataPacket clientDrawingDataPacket = new ClientDrawingDataPacket();
      clientDrawingDataPacket.x1 = this.x1;
      clientDrawingDataPacket.x2 = this.x2;
      clientDrawingDataPacket.y1 = this.y1;
      clientDrawingDataPacket.y2 = this.y2;
      clientDrawingDataPacket.color = this.color;

      return clientDrawingDataPacket;
    }

    public PacketBuilder addX1(Double x1){
      this.x1 = x1;
      return this;
    }

    public PacketBuilder addX2(Double x2){
      this.x2 = x2;
      return this;

    }

    public PacketBuilder addY1(Double y1){
      this.y1 = y1;
      return this;
    }

    public PacketBuilder addY2(Double y2){
      this.y2 = y2;
      return this;
    }

    public PacketBuilder addColor(Color color){
      this.color = color;
      return this;
    }
  }


}
