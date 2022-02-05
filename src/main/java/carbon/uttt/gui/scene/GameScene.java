package carbon.uttt.gui.scene;

import carbon.uttt.gui.controller.GameController;
import carbon.uttt.gui.game.IInteractiveGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameScene {
    public GameScene(Stage stage, IInteractiveGame game) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        loader.setControllerFactory(t -> new GameController(game));
        Scene scene = new Scene(loader.load(), -1, -1);
        stage.setScene(scene);
        stage.show();
    }
}
