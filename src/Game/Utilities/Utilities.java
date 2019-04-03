package Game.Utilities;

import Game.Packets.ConnectionRequestPacket;

import java.io.*;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;


// static class that contains utilities that deal with placing and extracting data
// to packets;

public class Utilities {

  public static int extractPacketSize(DatagramPacket d){
    return ByteBuffer.wrap(Arrays.copyOfRange(d.getData(),1,5)).getInt();
  }

  public static byte[] extractData(DatagramPacket d, int size){
    return  Arrays.copyOfRange(d.getData(),6,6+size);
  }

  public static Serializable deserializeObject(byte[] objectBytes) throws IOException, ClassNotFoundException {
    ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
    ObjectInput in = new ObjectInputStream(bis);

    Serializable crp = (Serializable) in.readObject();

    return crp;
  }

  public static byte[] convertObjectToBytes(byte s, Serializable message) throws IOException {
    byte[] stamp = new byte[1];
    byte[] data;

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    bos.write(stamp);
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(message);
    oos.flush();

    data = bos.toByteArray();

    // allocate four bytes for object size;
    byte[] dataSize = ByteBuffer.allocate(4).putInt(data.length).array();

    ByteArrayOutputStream baos= new ByteArrayOutputStream();

    baos.write(stamp);
    baos.write(dataSize);
    baos.write(data);

    data = baos.toByteArray();

    data[0] = s;

    return data;

  }

}
