package carbon.uttt;

import carbon.uttt.gui.scene.GameScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class UTTT extends Application {
    @Override
    public void start(Stage stage) {
        new GameScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
