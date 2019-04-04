package Game.Game;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Random;

public class Square {

    public static final int STATE_IDLE = 3;
    public static final int STATE_SELECTED = 0;
    public static final int STATE_CLAIMED = 2;
    public static final int STATE_EXITED = 1;

    private PixelReader pixelReader;
    private PixelWriter pixelWriter;
    private int sizex,sizey;
    private String entityID;
    private ImageView back;
    private WritableImage image;
    private double prevx,prevy;
    private Game game;

    private Random rand;

    private int state;

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

        clear();

        init();

    }

    private void init(){

        back.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(state == STATE_IDLE) {
                    //System.out.println(entityID);
                    state = STATE_SELECTED;
                }
                event.consume();
            }
        });

        back.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (state == STATE_SELECTED && game.getLock(entityID)){
				    if(prevx == -1 || prevy == -1) {
				        prevx = event.getX();
				        prevy = event.getY();
				    }

				    drawline(prevx-back.getX(),prevy-back.getY(),event.getX()-back.getX(),event.getY()-back.getY(),game.getUsrClr());
				    
				    game.sendTwoPointsforDrawLine(prevx-back.getX(),prevy-back.getY(),event.getX()-back.getX(),event.getY()-back.getY(),entityID);
				    
				    prevx = event.getX();
				    prevy = event.getY();
				}
                event.consume();
            }
        });



        back.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(state == STATE_EXITED) {
                    state = STATE_IDLE;
                    clear();
                }
                else if (state == STATE_SELECTED){
                    double count = 0;
                    for (int x = 0; x < sizex; x++) {
                        for (int y = 0; y < sizey; y++) {
                            if(!pixelReader.getColor(x, y).equals(Color.WHITE))
                                count++;
                        }
                    }
                    if((count/(sizex*sizey)) >= game.getThreashhold() && game.reqUnlock(entityID)){
                        state = STATE_CLAIMED;
                        fill(game.getUsrClr());
                        game.sendClaimedState(entityID);
                    }
                    else {
                    	game.sendDrawFail(entityID);
                    	game.reqUnlock(entityID);
                        clear();
                    }
                }
                System.out.println(entityID);//todo remove
                event.consume();
            }
        });

        back.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown() && state == STATE_SELECTED) {
                    double count = 0;
                    for (int x = 0; x < sizex; x++) {
                        for (int y = 0; y < sizey; y++) {
                            if(!pixelReader.getColor(x, y).equals(Color.WHITE))
                                count++;
                        }
                    }
                    if((count/(sizex*sizey)) >= game.getThreashhold() && game.reqUnlock(entityID)){
                        state = STATE_CLAIMED;
                        fill(game.getUsrClr());
                        game.sendClaimedState(entityID);
                    }
                    else {
                    	game.sendDrawFail(entityID);
                    	game.reqUnlock(entityID);
                        clear();
                        state = STATE_IDLE;
                    }

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
                drawSphere((int) (-x1), (int) (y1), c,game.getBrushSize());
            if(flip && negate)
                drawSphere((int) (-y1), (int) (x1), c,game.getBrushSize());
            if(flip && !negate)
                drawSphere((int) (y1), (int) (x1), c,game.getBrushSize());
            if(!flip && !negate)
                drawSphere((int) (x1), (int) (y1), c,game.getBrushSize());
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

    private void drawSphere(int x, int y, Color c,int size){
        for (int i = -size; i < size; i++){
            for (int j = -size;j < size; j++){
                if ((x - i) >= 0 && (x-i)< this.sizex){
                    if((y - j) >= 0 && (y-j)< this.sizey){
                        if( i*i+j*j <= size*size){
                            pixelWriter.setColor(x-i, y-j, c);
                        }
                    }
                }
            }
        }
    }

    public ImageView getImage(){
        return back;
    }

    public void setEntityID(String ID){
        entityID = ID;
    }

}
