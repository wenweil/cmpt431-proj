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

    private PixelReader pixelReader;
    private PixelWriter pixelWriter;
    private int sizex,sizey;
    private String entityID;
    private ImageView back;
    private WritableImage image;

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


        state = STATE_IDLE;

        this.rand = new Random();

        fill(Color.color(rand.nextDouble(),rand.nextDouble(),rand.nextDouble()));

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
                    System.out.println(event.getX()+","+event.getY());
                    //todo add network message and drawing of line from previous point
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
                    state = STATE_CLAIMED;
                    System.out.println(state);
                    fill(Color.color(rand.nextDouble(),rand.nextDouble(),rand.nextDouble()));
                    //todo add on mouse release behavior
                }else {
                    //clear();
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

    public void drawline(int x1, int y1, int x2, int y2, Color c){
        //todo implement drawline
    }

    private void clear (){
        for (int x =0 ; x < sizex ; x++){
            for (int y =0 ; y < sizey ; y++) {
                pixelWriter.setColor(x,y, Color.color(rand.nextDouble(),rand.nextDouble(),rand.nextDouble()));
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

    public ImageView getImage(){
        return back;
    }

}
