package Game.GameOld;

import Game.Packets.ClientDrawingDataPacket;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Square {

    public static final int STATE_IDLE = 3;
    public static final int STATE_SELECTED = 0;
    public static final int STATE_CLAIMED = 2;
    public static final int STATE_EXITED = 1;
    
    private static final double  THRESHHOLD = 0.5;

    private PixelReader pixelReader;
    private PixelWriter pixelWriter;
    private int sizex,sizey;
    private String entityID;
    private ImageView back;
    private WritableImage image;
    private double prevx,prevy;
    private Color userColor;
    private Game game;

    private Random rand;

    private int state;

    public static BlockingQueue<ClientDrawingDataPacket> outgoingBuffer;
    public static BlockingQueue<ClientDrawingDataPacket> incomingBuffer; // When to check the incomiing buffer for messages;
    // this has to be a separate thread that is fired every time mouse is realeased.

    public Square(int offsetx, int offsety, int sizex, int sizey, Game game) {


        this.sizex = sizex;
        this.sizey = sizey;

        this.image = new WritableImage(sizex,sizey);
        this.back = new ImageView(this.image);
        this.pixelReader = image.getPixelReader();
        this.pixelWriter = image.getPixelWriter();
        this.game = game;

        back.setX(offsetx);
        back.setY(offsety);
        
        prevx = -1;
        prevy = -1;


        state = STATE_IDLE;

        this.rand = new Random();

        outgoingBuffer = new ArrayBlockingQueue<>(2048);
        incomingBuffer = new ArrayBlockingQueue<>(2048);

        clear();
        
        init();

    }

    private void init(){
    	
        back.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(state == STATE_IDLE) {
                    state = STATE_SELECTED;
                }
                event.consume();
            }
        });

        back.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (state == STATE_SELECTED){
                    if(prevx == -1 || prevy == -1) {
                    	prevx = event.getX();
                    	prevy = event.getY();
                    }

                    ClientDrawingDataPacket clientDrawingDataPacket = new ClientDrawingDataPacket.PacketBuilder().
                        addX1(prevx).
                        addX2(event.getX()).
                        addY1(prevy).
                        addY2(event.getY()).
                        addColor(userColor).
                        build();

                  outgoingBuffer.add(clientDrawingDataPacket);
                  System.out.println("added clientDrawingDataPacket to buffer");
                    //drawline(prevx,prevy,event.getX(),event.getY(),userColor);
                    prevx = event.getX();
                    prevy = event.getY();
                }
                event.consume();
            }
        });



        back.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(state == STATE_EXITED)
                    state = STATE_IDLE;
                else if (state == STATE_SELECTED){
                    double count = 0;
                	for (int x = 0; x < sizex; x++) {
                        for (int y = 0; y < sizey; y++) {
                            if(!pixelReader.getColor(x, y).equals(Color.WHITE))
                            	count++;
                        }
                    }
                	if((count/(sizex*sizey)) >= THRESHHOLD){
        				state = STATE_CLAIMED;
        				fill(userColor);
        			}
                	else
                		clear();
                }

                while(!incomingBuffer.isEmpty()){
                  try {

                    ClientDrawingDataPacket p = incomingBuffer.take();
                    System.out.println("ClientDrawingDataPacket"+p);
                    //System.out.println(p);
                    //System.out.println("printing data"+p.getX1()+" "+p.getY1()+" "+p.getX2()+" "+p.getY2());
                    drawline(p.getX1(),p.getY1(),p.getX2(),p.getY2(),p.getColor());
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }

                }
              System.out.println("Mouse is released");
                event.consume();
            }
        });

        back.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown() && state == STATE_SELECTED) {
                    state = STATE_EXITED;
                }
                if (state == STATE_CLAIMED)

                    state = STATE_CLAIMED;
                else {
                    state = STATE_IDLE;
                }
                event.consume();
            }
        });
    }

    public void setState(int state){
        this.state = state;
    }

    public void drawline(double x1, double y1, double x2, double y2, Color c){
        double tmp;
        boolean flip = false;
        boolean negate = false;
    	if( y2 < y1) {
    		tmp = y2;
    		y2 = y1;
    		y1 = tmp;
    		tmp = x2;
    		x2 = x1;
    		x1 = tmp;
    	}

    	if((x2-x1) > 0) {
    		if((x2-x1) >= (y2-y1)) {

    		}else {
    			tmp = x1;
    			x1 = y1;
    			y1 = tmp;
    			tmp = y2;
    			y2 = x2;
    			x2 = tmp;
    			flip = true;
    		}

    	}else {

    		if(-(x2-x1) >= (y2-y1)) {
    			tmp = -x1;
    			x1 = y1;
    			y1 = tmp;
    			tmp = y2;
    			y2 = -x2;
    			x2 = tmp;
    			flip = true;
    			negate = true;
    		}else {
    			x1 = -x1;
    			x2 = -x2;
    			negate = true;
    		}
    	}

    	double m = (y2-y1)/(x2-x1);
        while(x1<= x2){

        	if(negate && !flip)
        		pixelWriter.setColor((int) (-x1-back.getX()), (int) (y1-back.getY()), c);
        	else if(flip && negate)
        		pixelWriter.setColor((int) (-y1-back.getX()), (int) (x1-back.getY()), c);
        	else if(flip && !negate)
        		pixelWriter.setColor((int) (y1-back.getX()), (int) (x1-back.getY()), c);
        	else
        		pixelWriter.setColor((int) (x1-back.getX()), (int) (y1-back.getY()), c);
            x1++;
            y1+=m;
        }

    }
    	

    private void clear (){
        for (int x =0 ; x < sizex ; x++){
            for (int y =0 ; y < sizey ; y++) {
                pixelWriter.setColor(x,y, Color.WHITE);
            }
        }
        prevx = -1;
        prevy = -1;

    }

    public void fill(Color c) {
        for (int x = 0; x < sizex; x++) {
            for (int y = 0; y < sizey; y++) {
                pixelWriter.setColor(x, y, c);
            }
        }
    }
    
    public void setUsrClr(Color userColor) {
    	this.userColor = userColor;
    }

    public ImageView getImage(){
        return back;
    }

    public void setEntityID(String ID){
        entityID = ID;
    }

}
