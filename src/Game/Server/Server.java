package Game.Server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Server {

  private static Server instance;

  static {
    try {
      instance = new Server(4446);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Server getInstance(){
    return instance;
  }

  private DatagramSocket udpSocket;

  Listener listener;
  Sender sender;

  // String is ip address and integer is port number;

  HashMap<String,Integer> connectedClients;

  public static BlockingQueue<DatagramPacket> outgoingQueue = new ArrayBlockingQueue<>(2048);

  GameData gameData;


  public GameData getGameData() {
    return gameData;
  }

  private Server(int port) throws SocketException, IOException {

    this.udpSocket = new DatagramSocket(port);

    this.listener = new Listener(this);
    this.sender = new Sender(this);

    this.gameData = new GameData();
  }

  public Listener getListener(){
    return listener;
  }

  public Sender getSender(){
    return sender;
  }


  public DatagramSocket getUdpSocket(){
    return this.udpSocket;
  }


  public static void main(String[] args) throws Exception {

    Server server = Server.getInstance();

    new Thread(server.listener).start();
    new Thread(server.sender).start();

  }

  public void setGameData(GameData gameData){
    this.gameData = gameData;
  }


  }
