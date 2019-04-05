package Game;

import Game.Client.Client;
import Game.Client.GameStages.Connection;
import Game.Game.Game;
import Game.Packets.SquareStringResponse;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    public static Boolean isReady = false;
    public static boolean connection = true;
    public static boolean boardInit = false;
    public static boolean squareInit = false;
    public static boolean inProgress = false;
    public static boolean termination = false;
    public static Pane root = new Pane();
    public static Game game = new Game(root);
    public static String serverIP;
    public static boolean error = false;
    public static int boardWidth;
    public static int boardHeight;
    public static int numboxint;
    public static final int serverPort = 4446;
    public static List<SquareStringResponse> squareResponses = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("CMPT 431 - Project");

        VBox input = new VBox();

        TextField IPField = new TextField();
        TextField brushSizeField = new TextField();
        TextField numbox = new TextField();
        TextField thresh = new TextField();

        //creates the fields and the buttons on the screen for the first screen

        brushSizeField.setPromptText("Enter brush size here");
        numbox.setPromptText("Enter number of boxes here");
        thresh.setPromptText("Enter threshold here (double)");
        IPField.setPromptText("Enter server ip to connect to");

        Button connect = new Button("Connect!");

        Button newGame = new Button("Create Game!");

        newGame.setOnAction(new EventHandler<ActionEvent>() {

            //sets on clicking the new game button create the server client, the client that also acts as the server
            @Override
            public void handle(ActionEvent event) {
                numboxint = Integer.parseInt(numbox.getText());
                game.setBrushSize(Integer.parseInt(brushSizeField.getText()));
                game.setThreashhold(Double.parseDouble(thresh.getText()));
                game.setNumSqInCol(numboxint);
                game.setNumSqInRow(numboxint);
                boardHeight = 10 + 63 * numboxint;
                boardWidth = 10 + 63 * numboxint;

                //sets all the parameter of the game on startup

                WritableImage image = new WritableImage(10 + 63 * numboxint, 10 + 63 * numboxint);
                PixelWriter writer = image.getPixelWriter();

                for (int x = 0; x < 10 + 63 * numboxint; x++) {
                    for (int y = 0; y < 10 + 63 * numboxint; y++) {
                        writer.setColor(x, y, Color.GRAY);
                    }
                }
                //create the background
                root.getChildren().add(new ImageView(image));

                game.serverStart();

                Scene scene = new Scene(root, boardHeight, boardWidth);

                primaryStage.setScene(scene);

                primaryStage.centerOnScreen();
            }
        });


        connect.setOnAction(new EventHandler<ActionEvent>() {
            //set on clicking the connect button, connect to the server client. This does not start the thread associated
            //with server behaviors
            @Override
            public void handle(ActionEvent event) {

                try {
                    error = false;
                    serverIP = IPField.getText();
                    Client client = Client.getInstance();
                }
                catch(Exception exc){
                    exc.printStackTrace();
                    error = true;
                }

                if (!error){

                	Thread th = new Thread(new Connection());
                	//create the thread to get all information required to create the same game as the server, gets
                    //board size(number of squares),screen size,threshold, and all the entity IDs associated with the
                    //squares
                    th.start();
					try {
						th.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					


                    primaryStage.setTitle("CMPT 431 - Project");

                    WritableImage image = new WritableImage(boardWidth,boardHeight);
                    PixelWriter writer = image.getPixelWriter();


                    for (int x = 0; x < boardWidth; x++) {
                        for (int y = 0; y < boardHeight; y++) {
                            writer.setColor(x, y, Color.GRAY);
                        }
                    }

                    root.getChildren().add(new ImageView(image));

                    // initalize squares;
                    for(SquareStringResponse res: squareResponses){
                        int col = res.getColNumber();
                        int row = res.getRowNumber();
                        String s = res.getCode();
                        System.out.println(s);
                        game.addSquare(s,row,col);
                    }
                    //builds the board and add to current view.

                    Scene scene = new Scene(root, boardWidth, boardHeight);
                    primaryStage.setScene(scene);

                    primaryStage.show();
                }
            }
        });

        input.getChildren().addAll(brushSizeField,thresh,numbox,newGame,IPField,connect);

        Scene startup = new Scene (input,200,150);

        input.requestFocus();

        primaryStage.setScene(startup);

        primaryStage.show();


        //      WritableImage image = new WritableImage(1600,900);
//
//        PixelReader reader = image.getPixelReader();
//        PixelWriter writer = image.getPixelWriter();
//
//        for (int x = 0; x < 1600; x++) {
//            for (int y = 0; y < 900; y++) {
//                writer.setColor(x, y, Color.GRAY);
//            }
//        }
//
//        Pane root = new Pane();
//        root.getChildren().add(new ImageView(image));
//
//
//        Game g = new Game(root);
//
//        // Fire up the client
//
//        g.start();
//
//        Scene scene = new Scene(root, 1600, 900);
//
//        primaryStage.setScene(scene);
//
//        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
    }
}
