package Game.GameOld;

import Game.Packets.ClientDrawingDataPacket;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Game {
    private final Pane root;

    private HashMap<String, Square> objects;

    private BlockingQueue<ClientDrawingDataPacket> outgoingBuffer;
    private BlockingQueue<ClientDrawingDataPacket> incomingBuffer;

    public Game(Pane root){
        this.root = root;
    }

    public void start(){
        objects = new HashMap<>();

        int numInRow = 20;
        int numInCol= 14;
        Random r = new Random();
        Color c = new Color(r.nextDouble(),r.nextDouble(),r.nextDouble(),1.0);

        Square s;
        for(int i = 0; i < numInRow;i++){
            for(int j = 0; j < numInCol; j++){
                s = new Square(10+63*i,10+63*j,50,50,this);
                byte[] array = new byte[7]; // length is bounded by 7
                new Random().nextBytes(array);
                String generatedString = new String(array, Charset.forName("UTF-8"));
                s.setEntityID(generatedString);
                s.setUsrClr(c);
                objects.put(generatedString,s);
                root.getChildren().addAll(s.getImage());
            }
        }
    }

}
