import javafx.scene.image.WritableImage;

public interface networkManager {
    void sendMessage(String str);
    String receiveMessage();
    void sendMessageReliable(String str);
    String receiveMessageReliable();
    void sendGameState(Game game);
    Game receiveGameState();
    void sendImage(WritableImage img);
    WritableImage receiveImage();
    boolean getMutexByID(String ID);

}
