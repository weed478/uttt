package carbon.uttt.gui.controller;

import carbon.uttt.gui.game.GameConfiguration;
import carbon.uttt.gui.game.IInteractiveGame;
import carbon.uttt.gui.game.PvAIGame;
import carbon.uttt.gui.game.PvPGame;
import carbon.uttt.gui.scene.GameScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartController {

    @FXML
    public Node root;

    @FXML
    public CheckBox timeLimitCB;

    @FXML
    public Node timeLimitContainer;

    @FXML
    public TextField timeLimitTF;

    @FXML
    public void initialize() {
        timeLimitContainer.visibleProperty()
                .bind(timeLimitCB.selectedProperty());
    }

    @FXML
    public void onPressPvP(ActionEvent e) {
        launchGameScene(new PvPGame());
    }

    @FXML
    public void onPressPvAI(ActionEvent e) {
        launchGameScene(new PvAIGame());
    }

    private void launchGameScene(IInteractiveGame game) {
        Stage stage = (Stage) root.getScene().getWindow();
        try {
            GameConfiguration configuration = new GameConfiguration(
                    game,
                    timeLimitCB.isSelected() ? timeLimitTF.getText() : null
            );
            new GameScene(stage, configuration);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Couldn't start game");
            alert.setContentText(e.toString());
            alert.show();
        }
    }
}
