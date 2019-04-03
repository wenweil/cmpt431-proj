package Game.Client;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Client{

  private static Client instance;
  public static BlockingQueue<DatagramPacket> outgoingPackets = new ArrayBlockingQueue<>(512);

  // create static client instance
  static {
    try {
      instance = new Client("127.0.0.1",4446,4445);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // method to retrieve the instance
  public static Client getInstance(){
    return instance;
  }


  public InetAddress getServerAddress() {
    return serverAddress;
  }

  // Private fields for socket and server Address;
  private DatagramSocket clientSocket;
  private InetAddress serverAddress;
  private int serverPort;


  public DatagramSocket getClientSocket() {
    return clientSocket;
  }

  public int getServerPort() {
    return serverPort;
  }

  // Client that sends generated values to the
  private Client(String serverAddress, int serverPort, int localPort) throws SocketException, UnknownHostException {

    clientSocket = new DatagramSocket(localPort);
    this.serverAddress = InetAddress.getByName(serverAddress);
    this.serverPort = serverPort;

    // start listner and sender threads;
    new Thread(new Listener()).start();
    new Thread(new Sender()).start();

  }

  }