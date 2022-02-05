package carbon.uttt;

import carbon.uttt.gui.scene.StartScene;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class UTTT extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new StartScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
