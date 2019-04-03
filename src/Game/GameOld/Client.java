package Game.GameOld;

import Game.Packets.ClientDrawingDataPacket;

import java.io.*;
import java.net.*;

public class Client implements Runnable{
  private DatagramSocket socket;
  private InetAddress address;

  private byte[] buf;

  // Client that sends generated values to the
  public Client(String serverAddress) throws SocketException, UnknownHostException {
    socket = new DatagramSocket();
    address = InetAddress.getByName(serverAddress);
  }

  public void send() throws IOException, InterruptedException {

    // Need to think how to redesign this Class.
    //


    //System.out.println("inside send methdod");
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);

    //System.out.println("waiting for queue to fill in");
    ClientDrawingDataPacket p = Square.outgoingBuffer.take();

    oos.writeObject(p);

    oos.flush();

    byte [] data = bos.toByteArray();


    DatagramPacket packet
        = new DatagramPacket(data, data.length, address, 4445);

    System.out.println(data.length);

    socket.send(packet);


  }


  public class MulticastReceiver implements Runnable {
    protected MulticastSocket socket = null;

    public void run() {
      System.out.println("Starting Multicast Reciever");
      try {
        byte[] buf = new byte[245];
        socket = new MulticastSocket(4446);
        InetAddress group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        while (true) {
          DatagramPacket packet = new DatagramPacket(buf, buf.length);
          socket.receive(packet);

          ByteArrayInputStream in = new ByteArrayInputStream(packet.getData());


            ObjectInputStream is = new ObjectInputStream(in);
            ClientDrawingDataPacket p = (ClientDrawingDataPacket) is.readObject();
            Square.incomingBuffer.add(p);
            if(Main.cVarClientReciever=false){break;}
        }
        socket.leaveGroup(group);
        socket.close();
      }
      catch (IOException e){} catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  public void close() {
    socket.close();
  }

  public void run(){

    MulticastReceiver mr = new MulticastReceiver();
    Thread tr = new Thread(mr);
    tr.start();

    System.out.println("thread created");
    while(Main.cVarClientSender =true){
      try {
        send();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    close();
  }




}