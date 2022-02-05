package carbon.uttt.gui.controller;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;
import carbon.uttt.gui.MouseLocator;
import carbon.uttt.gui.game.IInteractiveGame;
import carbon.uttt.gui.game.IInteractiveGameObserver;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * Game scene controller.
 */
public class GameController implements IInteractiveGameObserver {

    /**
     * Game instance this controller is using.
     */
    private final IInteractiveGame game;

    /**
     * Locates the mouse on the game board.
     */
    private MouseLocator mouseLocator;

    /**
     * Main board canvas.
     */
    @FXML
    public Canvas canvas;

    /**
     * History list.
     * First row has column titles.
     * Rows from index 1 have next turns.
     */
    @FXML
    public GridPane historyGridPane;

    /**
     * History grid pane is placed in a scroll pane.
     */
    @FXML
    public ScrollPane historyScrollPane;

    /**
     * Label indicating the current player is X.
     * Hidden by default.
     */
    @FXML
    public Label currentPlayerXLabel;

    /**
     * Label indicating the current player is Y.
     * Hidden by default.
     */
    @FXML
    public Label currentPlayerOLabel;

    /**
     * Set value to update current player labels visibility.
     * Must be set from UI thread.
     */
    private final BooleanProperty currentPlayerIsX = new SimpleBooleanProperty(true);

    /**
     * Creates a game session operating on given game instance.
     * @param game Game to run.
     */
    public GameController(IInteractiveGame game) {
        this.game = game;
        game.addGameObserver(this);
    }

    /**
     * Setup UI after FXML bindings.
     */
    @FXML
    public void initialize() {
        // set game to initial state
        game.reset();

        // setup mouse events
        mouseLocator = new MouseLocator(
                canvas.widthProperty(),
                canvas.heightProperty()
        );
        canvas.setOnMouseClicked(this::onMouseClicked);
        canvas.setOnMouseMoved(this::onMouseMoved);
        canvas.setOnMouseExited(this::onMouseExited);

        // enable autoscroll on history list
        historyScrollPane.vvalueProperty().bind(historyGridPane.heightProperty());

        // setup current player display
        currentPlayerXLabel
                .visibleProperty()
                .bind(currentPlayerIsX);
        currentPlayerOLabel
                .visibleProperty()
                .bind(currentPlayerXLabel.visibleProperty().not());
    }

    /**
     * Handle canvas clicks.
     */
    private void onMouseClicked(MouseEvent e) {
        // make move if user clicked valid field
        Pos9x9 move = mouseLocator.locateMouse(e.getX(), e.getY());
        if (game.moveValid(move)) {
            game.makeMove(move);
        }
    }

    /**
     * Handle mouse moving over canvas.
     */
    private void onMouseMoved(MouseEvent e) {
        // highlight field under cursor if available
        Pos9x9 move = mouseLocator.locateMouse(e.getX(), e.getY());
        game.highlightMove(move);
    }

    /**
     * Mouse left canvas.
     */
    private void onMouseExited(MouseEvent e) {
        // clear all highlights
        game.highlightMove(null);
    }

    /**
     * Redraw board.
     * Must be run on UI thread.
     */
    private void updateUI() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        // scale entire canvas to [0, 1]
        gc.scale(canvas.getWidth(), canvas.getHeight());
        // clear canvas
        gc.clearRect(0, 0, 1, 1);
        game.draw(gc);
        gc.restore();
    }

    @Override
    public void onGameNeedsRedraw(IInteractiveGame game) {
        if (game != this.game) return;

        runOnUI(this::updateUI);
    }

    @Override
    public void onMoveMade(IInteractiveGame game, Player player, Pos9x9 pos) {
        if (game != this.game) return;
        // add move made to history
        runOnUI(() -> addHistoryItem(player, pos));
    }

    @Override
    public void onCurrentPlayerChanged(IInteractiveGame game, Player newPlayer) {
        if (game != this.game) return;
        // update current player display
        runOnUI(() -> currentPlayerIsX.set(newPlayer == Player.X));
    }

    /**
     * Add item to move history list.
     * Must be called from UI thread.
     * @param player Player who made the move.
     * @param pos Move position.
     */
    private void addHistoryItem(Player player, Pos9x9 pos) {
        historyGridPane.addRow(
                historyGridPane.getRowCount(),
                new Label(String.valueOf(historyGridPane.getRowCount())),
                new Label(player.toString()),
                new Label(pos.toString())
        );
    }

    /**
     * Run function right now if current thread is UI
     * or schedule function on UI thread.
     * @param f Function to run.
     */
    private void runOnUI(Runnable f) {
        if (Platform.isFxApplicationThread()) {
            f.run();
        }
        else {
            Platform.runLater(f);
        }
    }
}
