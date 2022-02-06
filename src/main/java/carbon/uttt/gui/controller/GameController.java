package carbon.uttt.gui.controller;
import carbon.uttt.ai.RandomAI;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;
import carbon.uttt.gui.MouseLocator;
import carbon.uttt.gui.game.GameConfiguration;
import carbon.uttt.gui.game.IInteractiveGame;
import carbon.uttt.gui.game.IInteractiveGameObserver;
import carbon.uttt.gui.scene.StartScene;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

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
     * Null or time limit for this game session.
     */
    private final Integer timeLimitSeconds;

    /**
     * Container of the time limit display.
     * Used to hide the time limit when not used.
     */
    @FXML
    public Node timeLimitContainer;

    /**
     * Shows current time limit.
     */
    @FXML
    public Label timeLimitLabel;

    /**
     * Counts down to the end of move time limit.
     */
    private Timer timeLimitTimeoutTimer = null;

    /**
     * Updates time limit label every second.
     */
    private Timer updateTimeLimitLabelTimer = null;

    /**
     * Creates a game session with given configuration.
     * @param configuration Game configuration.
     */
    public GameController(GameConfiguration configuration) {
        this.game = configuration.game;
        this.timeLimitSeconds = configuration.timeLimitSeconds;
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

        // prepare time limit label
        if (timeLimitSeconds != null) {
            timeLimitLabel.setText(timeLimitSeconds.toString());
        }
    }

    /**
     * Handle canvas clicks.
     */
    private void onMouseClicked(MouseEvent e) {
        // make move if user clicked valid field
        Pos9x9 move = mouseLocator.locateMouse(e.getX(), e.getY());
        if (game.moveValid(move)) {
            stopTimers();
            game.makeMove(move);
            if (!game.isGameOver() && timeLimitSeconds != null) {
                // time limit visible only after first move
                timeLimitContainer.setVisible(true);
                beginTurnTimeout();
            }
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
     * New game button.
     */
    @FXML
    public void onClickNewGame(ActionEvent e) {
        launchNewGame();
    }

    /**
     * Open new StartScene.
     */
    private void launchNewGame() {
        cleanup();
        try {
            new StartScene((Stage) canvas.getScene().getWindow());
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Couldn't start new game");
            alert.setContentText(e.toString());
            alert.show();
        }
    }

    /**
     * Safely shutdown game.
     */
    private void cleanup() {
        game.removeGameObserver(this);
        stopTimers();
    }

    private void stopTimers() {
        if (timeLimitTimeoutTimer != null)
            timeLimitTimeoutTimer.cancel();

        if (updateTimeLimitLabelTimer != null)
            updateTimeLimitLabelTimer.cancel();
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

    /**
     * Start turn time limit countdown.
     */
    private void beginTurnTimeout() {
        new Thread(() -> {
            // prepare time limit label
            runOnUI(() -> timeLimitLabel.setText(timeLimitSeconds.toString()));

            // cancel previous timers
            stopTimers();

            // update label every second
            updateTimeLimitLabelTimer = new Timer(true);
            updateTimeLimitLabelTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    updateTimeLimitLabel();
                }
            }, 1000, 1000);

            // end turn after time limit
            timeLimitTimeoutTimer = new Timer(true);
            timeLimitTimeoutTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onTimeLimitReached();
                }
            }, 1000L * timeLimitSeconds);
        }).start();
    }

    private void updateTimeLimitLabel() {
        runOnUI(() -> {
            int oldTime = Integer.parseInt(timeLimitLabel.getText());
            int newTime = Math.max(0, oldTime - 1);
            timeLimitLabel.setText(Integer.toString(newTime));
        });
    }

    private void onTimeLimitReached() {
        // make random move instead
        Pos9x9 move = new RandomAI(game).decideMove();
        game.makeMove(move);
        if (!game.isGameOver()) {
            // next player's turn
            beginTurnTimeout();
        }
    }
}
