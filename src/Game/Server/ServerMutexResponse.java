package Game.Server;

import Game.Main;
import Game.Packets.MutexRequestPacket;
import Game.Packets.MutexResponsePacket;
import Game.Strategies.stamps;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;

import static Game.Utilities.Utilities.convertObjectToBytes;

public class ServerMutexResponse {
    ServerMutexResponse(){

    }

    public DatagramPacket generateServerResponse(InetAddress address, int port, Serializable requestObjec){
        byte[] data;
        MutexRequestPacket mp = (MutexRequestPacket) requestObjec;
        Boolean Retval = Main.game.getMutex(mp.getSquareCode()).reqLock(mp.getUserID());
        MutexResponsePacket mpr = new MutexResponsePacket(mp.getSquareCode(),Retval);
        byte stamp = stamps.MUTEXLOCKRESPONSE.val();
        try {
            data = convertObjectToBytes(stamp,mpr);
            return new DatagramPacket(data,data.length,address,port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
