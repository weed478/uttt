package carbon.uttt.gui.controller;
import carbon.uttt.game.Pos9x9;
import carbon.uttt.gui.IDrawable;
import carbon.uttt.gui.IDrawableObserver;
import carbon.uttt.gui.MouseLocator;
import carbon.uttt.gui.game.InteractiveGame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class GameController implements IDrawableObserver {

    private final InteractiveGame game = new InteractiveGame();
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

        game.reset();

        canvas.setOnMouseClicked(this::onMouseClicked);
        canvas.setOnMouseMoved(this::onMouseMoved);
        canvas.setOnMouseExited(this::onMouseExited);
    }

    private void onMouseClicked(MouseEvent e) {
        Pos9x9 dpmp = mouseLocator.locateMouse(e.getX(), e.getY());
        game.makeMove(dpmp);
    }

    private void onMouseMoved(MouseEvent e) {
        Pos9x9 dpmp = mouseLocator.locateMouse(e.getX(), e.getY());
        game.highlightMove(dpmp);
    }

    private void onMouseExited(MouseEvent e) {
        game.highlightMove(null);
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
