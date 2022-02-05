package carbon.uttt.gui.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScene {

    public StartScene(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(StartScene.class.getResource("start.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}
