package Game.GameOld;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static boolean cVarClientSender = true;
    public static boolean cVarClientReciever = true;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("CMPT 431 - Project");

        WritableImage image = new WritableImage(1600,900);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = image.getPixelWriter();

        for (int x = 0; x < 1600; x++) {
            for (int y = 0; y < 900; y++) {
                writer.setColor(x, y, Color.GRAY);
            }
        }

        Pane root = new Pane();
        root.getChildren().add(new ImageView(image));

        Game g = new Game(root);
        g.start();
        Scene scene = new Scene(root, 1600, 900);

        primaryStage.setScene(scene);
        primaryStage.show();

        // initalize the client
        Client client = new Client("127.0.1.1");
        Thread cl = new Thread(client);
        cl.start();




    }


    public static void main(String[] args) {
        launch(args);
    }
}
