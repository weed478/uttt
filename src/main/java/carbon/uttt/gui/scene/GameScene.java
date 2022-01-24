package carbon.uttt.gui.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScene {
    public GameScene(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 400);
        } catch (Exception e) {
            throw new RuntimeException("Error loading game scene");
        }
        stage.setScene(scene);
        stage.show();
    }
}
