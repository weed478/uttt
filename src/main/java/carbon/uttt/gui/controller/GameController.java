package carbon.uttt.gui.controller;

import carbon.uttt.game.Game;
import carbon.uttt.gui.IDrawable;
import carbon.uttt.gui.IDrawableObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameController implements IDrawableObserver {

    private final Game game = new Game();

    @FXML
    public Canvas canvas;

    public GameController() {
        game.addDrawableObserver(this);
    }

    @FXML
    public void initialize() {
        game.newGame();
    }

    public void updateUI() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        gc.scale(canvas.getWidth(), canvas.getHeight());
        game.draw(gc);
        gc.restore();
    }

    @Override
    public void onDrawableStale(IDrawable drawable) {
        if (drawable == game) {
            if (Platform.isFxApplicationThread()) {
                updateUI();
            } else {
                Platform.runLater(this::updateUI);
            }
        }
    }
}
