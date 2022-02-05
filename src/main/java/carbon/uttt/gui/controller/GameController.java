package carbon.uttt.gui.controller;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;
import carbon.uttt.gui.MouseLocator;
import carbon.uttt.gui.game.IInteractiveGame;
import carbon.uttt.gui.game.IInteractiveGameObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameController implements IInteractiveGameObserver {

    private final IInteractiveGame game;
    private MouseLocator mouseLocator;

    @FXML
    public Canvas canvas;

    @FXML
    public GridPane historyGridPane;

    @FXML
    public ScrollPane historyScrollPane;

    public GameController(IInteractiveGame game) {
        this.game = game;
        game.addGameObserver(this);
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

        historyScrollPane.vvalueProperty().bind(historyGridPane.heightProperty());
    }

    private void onMouseClicked(MouseEvent e) {
        Pos9x9 move = mouseLocator.locateMouse(e.getX(), e.getY());
        if (game.moveValid(move)) {
            game.makeMove(move);
        }
    }

    private void onMouseMoved(MouseEvent e) {
        Pos9x9 move = mouseLocator.locateMouse(e.getX(), e.getY());
        game.highlightMove(move);
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
    public void onGameNeedsRedraw(IInteractiveGame game) {
        if (game != this.game) return;

        if (Platform.isFxApplicationThread()) {
            updateUI();
        } else {
            Platform.runLater(this::updateUI);
        }
    }

    @Override
    public void onMoveMade(IInteractiveGame game, Player player, Pos9x9 pos) {
        if (game != this.game) return;

        if (Platform.isFxApplicationThread()) {
            addHistoryItem(player, pos);
        } else {
            Platform.runLater(() -> addHistoryItem(player, pos));
        }
    }

    private void addHistoryItem(Player player, Pos9x9 pos) {
        historyGridPane.addRow(
                historyGridPane.getRowCount(),
                new Label(String.valueOf(historyGridPane.getRowCount())),
                new Label(player.toString()),
                new Label(pos.toString())
        );
    }
}
