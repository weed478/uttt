package carbon.uttt.gui.controller;

import carbon.uttt.game.BoardDP;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos3x3;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameController {

    private final BoardDP board = new BoardDP();

    @FXML
    public Canvas canvas;

    @FXML
    public void initialize() {
        board.getBoardMP(Pos3x3.NE).putPlayer(Pos3x3.SW, Player.X);
        board.getBoardMP(Pos3x3.C).putPlayer(Pos3x3.NE, Player.O);
        drawBoard();
    }

    private void drawBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        gc.scale(canvas.getWidth(), canvas.getHeight());
        board.draw(gc);
        gc.restore();
    }
}
