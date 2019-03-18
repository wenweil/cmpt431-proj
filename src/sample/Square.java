package sample;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.Random;

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

    private Random rand;

    private int state;

    public Square(int offsetx, int offsety, int sizex, int sizey, String entityID) {


        this.sizex = sizex;
        this.sizey = sizey;

        this.image = new WritableImage(sizex,sizey);
        this.back = new ImageView(this.image);
        this.pixelReader = image.getPixelReader();
        this.pixelWriter = image.getPixelWriter();

        back.setX(offsetx);
        back.setY(offsety);
        
        prevx = -1;
        prevy = -1;


        state = STATE_IDLE;

        this.rand = new Random();

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
                    drawline(prevx,prevy,event.getX(),event.getY(),userColor);
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
                    int count = 0;
                	for (int x = 0; x < sizex; x++) {
                        for (int y = 0; y < sizey; y++) {
                            if(!pixelReader.getColor(x, y).equals(Color.WHITE))
                            	count++;
                        }
                    }
                	
                	if(count/(sizex*sizey) >= THRESHHOLD){
        				state = STATE_CLAIMED;
        				fill(userColor);
        			}
                	else
                		clear();
                }
                event.consume();
            }
        });

        back.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown() && state == STATE_SELECTED) {
                    state = STATE_EXITED;
                    System.out.println(state);
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
        
    	
    }

    private void clear (){
        for (int x =0 ; x < sizex ; x++){
            for (int y =0 ; y < sizey ; y++) {
                pixelWriter.setColor(x,y, Color.WHITE);
            }
        }

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

}
