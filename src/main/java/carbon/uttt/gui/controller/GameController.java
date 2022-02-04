package carbon.uttt.gui.controller;

import carbon.uttt.game.Game;
import carbon.uttt.game.PosDPMP;
import carbon.uttt.gui.IDrawable;
import carbon.uttt.gui.IDrawableObserver;
import carbon.uttt.gui.MouseLocator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class GameController implements IDrawableObserver {

    private final Game game = new Game();
    private MouseLocator mouseLocator;

    @FXML
    public Canvas canvas;

    public GameController() {
        game.addDrawableObserver(this);
    }

    @FXML
    public void initialize() {
        mouseLocator = new MouseLocator(
                canvas.widthProperty(),
                canvas.heightProperty()
        );

        game.newGame();

        canvas.setOnMouseClicked(this::onMouseClicked);
        canvas.setOnMouseMoved(this::onMouseMoved);
        canvas.setOnMouseExited(this::onMouseExited);
    }

    private void onMouseClicked(MouseEvent e) {
        PosDPMP dpmp = mouseLocator.locateMouse(e.getX(), e.getY());
        game.makeMoveDPMP(dpmp);
    }

    private void onMouseMoved(MouseEvent e) {
        PosDPMP dpmp = mouseLocator.locateMouse(e.getX(), e.getY());
        game.highlightMoveDPMP(dpmp);
    }

    private void onMouseExited(MouseEvent e) {
        game.highlightMoveDPMP(null);
    }

    private void updateUI() {
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
