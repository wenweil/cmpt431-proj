package Game.Server;

import Game.Main;
import Game.Game.Square;
import Game.Game.Game.Tuple;
import Game.Packets.ClientDrawingDataPacket;
import Game.Packets.SendStringPacket;
import Game.Strategies.stamps;
import Game.Utilities.Utilities;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Listener implements Runnable {

  private Server server;
  private HashMap<String,ServerResponseStrategy> strategies;

  Listener(Server server) throws SocketException {
    this.server = server;
    strategies = new HashMap<>();

    strategies.put("ServerConnectionResponse", new ServerConnectionResponse());

    // expand as new strategies are added;
  }

  public void run(){
    try {

      while (true) {

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // blocks until a packet is received
        DatagramSocket udpSocket = server.getUdpSocket();

        udpSocket.receive(packet);

        processIncomingPacket(packet);

      }

    } catch (IOException|ClassNotFoundException i) {
      i.printStackTrace();
    }
  }


  public void processIncomingPacket(DatagramPacket packet) throws IOException, ClassNotFoundException {
    class ProcessPacket implements Runnable{

      DatagramPacket packet;
      ProcessPacket(DatagramPacket packet){
        this.packet = packet;
      }


      public void run(){
        byte[] packetData = packet.getData();

        InetAddress remoteAddress = packet.getAddress();
        int remotePort = packet.getPort();

        // extract stamp
        byte stamp = packetData[0];

        // extract data size
        int size = Utilities.extractPacketSize(packet);

        // get data
        byte[] data = Utilities.extractData(packet,size);

        try {
          Serializable object = Utilities.deserializeObject(data);

          if (stamp == 1){
            DatagramPacket dp = strategies.get("ServerConnectionResponse").generateServerResponse(remoteAddress,remotePort,object);
            Server.outgoingQueue.put(dp);
            String s = "";
            for(Tuple t: Main.game.tuples) {
            	InetAddress x = (InetAddress) t.y;
            	int port = (int) t.z;
            	s += x.getHostAddress() + ":" +port+"|";
            }
            
            System.out.println(s);
            
            SendStringPacket p = new SendStringPacket(s);
            byte st = stamps.UPDATELISTOFUSER.val();
            byte[] dat = Utilities.convertObjectToBytes(st,p);
            for(Tuple t: Main.game.tuples) {
            	InetAddress x = (InetAddress) t.y;
            	int port = (int) t.z;
            	System.out.println(x.getHostAddress()+":"+port);
            	Server.outgoingQueue.put(new DatagramPacket(dat,dat.length,x,port));
            }
            

          }
          else if (stamp == 3){
              DatagramPacket dp = new ServerStringResponse().generateServerResponse(remoteAddress,remotePort,object);
              Server.outgoingQueue.put(dp);
            }
            else if(stamp == 5){
              DatagramPacket dp  = new ServerMutexResponse().generateServerResponse(remoteAddress,remotePort,object);
              Server.outgoingQueue.put(dp);

            }
            else if(stamp == 7){
              DatagramPacket dp = new ServerMutexReleaseResponse().generateServerResponse(remoteAddress,remotePort,object);
              Server.outgoingQueue.put(dp);
            }
          else if (stamp == stamps.DRAWINGDATA.val()) {
        	  ClientDrawingDataPacket p = (ClientDrawingDataPacket) object;
        	  HashMap<String,Square> squares = Main.game.getSquare();
        	  squares.get(p.getEID()).drawline(p.getX1(), p.getY1(), p.getX2(), p.getY2(), p.getColor());
        	  byte[] dat = Utilities.convertObjectToBytes(stamps.DRAWINGDATA.val(), p);
        	  for(Tuple t : Main.game.tuples) {
					try {
						InetAddress IP = (InetAddress)t.y;
						int port = (int) t.z;
						if(IP.equals(remoteAddress))
							continue;
						else
							Server.outgoingQueue.put(new DatagramPacket(dat,dat.length,IP,port));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
        	  
          }else if (stamp == stamps.DRAWFAIL.val()) {
        	  SendStringPacket p = (SendStringPacket) object;
        	  HashMap<String,Square> squares = Main.game.getSquare();
        	  squares.get(p.getString()).clear();
        	  squares.get(p.getString()).setState(Square.STATE_IDLE);
        	  byte[] dat = Utilities.convertObjectToBytes(stamps.DRAWFAIL.val(), p);
        	  for(Tuple t : Main.game.tuples) {
					try {
						InetAddress IP = (InetAddress)t.y;
						int port = (int) t.z;
						if(IP.equals(remoteAddress))
							continue;
						else
							Server.outgoingQueue.put(new DatagramPacket(dat,dat.length,IP,port));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
        	  
          }else if(stamp == stamps.DRAWCLAIM.val()) {
        	  SendStringPacket p = (SendStringPacket) object;
        	  HashMap<String,Square> squares = Main.game.getSquare();
        	  String eid = p.getString().split(";")[0];
        	  String color = p.getString().split(";")[1];
        	  squares.get(eid).fill(Color.valueOf(color));;
        	  squares.get(eid).setState(Square.STATE_CLAIMED);
        	  byte[] dat = Utilities.convertObjectToBytes(stamps.DRAWCLAIM.val(), p);
        	  for(Tuple t : Main.game.tuples) {
					try {
						InetAddress IP = (InetAddress)t.y;
						int port = (int) t.z;
						if(IP.equals(remoteAddress))
							continue;
						else
							Server.outgoingQueue.put(new DatagramPacket(dat,dat.length,IP,port));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
          }


        }
        catch (IOException |ClassNotFoundException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }


      }
    }

    new Thread(new ProcessPacket(packet)).start();
  }

}
