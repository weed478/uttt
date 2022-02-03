package carbon.uttt.gui.controller;

import carbon.uttt.game.Game;
import carbon.uttt.game.Pos3x3;
import carbon.uttt.game.PosDPMP;
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
        game.makeMove(new PosDPMP(Pos3x3.C, Pos3x3.NE));
        game.makeMove(new PosDPMP(Pos3x3.NE, Pos3x3.NE));
        game.makeMove(new PosDPMP(Pos3x3.NE, Pos3x3.C));
        game.makeMove(new PosDPMP(Pos3x3.C, Pos3x3.N));
    }

    public void updateUI() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        gc.scale(canvas.getWidth(), canvas.getHeight());
        gc.clearRect(0, 0, 1, 1);
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
