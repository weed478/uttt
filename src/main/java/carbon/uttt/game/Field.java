package carbon.uttt.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Field implements IField {

    private Player player;

    public Field() {
        this.player = Player.EMPTY;
    }

    public void setPlayer(Player p) {
        if (p == Player.EMPTY) {
            throw new IllegalArgumentException("Cannot clear field");
        }
        if (player != Player.EMPTY) {
            throw new IllegalArgumentException("Cannot change occupied field");
        }
        player = p;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.1);
        gc.strokeRect(0, 0, 1, 1);

        gc.setLineWidth(0.1);

        if (player == Player.X) {
            gc.beginPath();
            gc.moveTo(0.3, 0.3);
            gc.lineTo(0.7, 0.7);
            gc.moveTo(0.7, 0.3);
            gc.lineTo(0.3, 0.7);

            gc.setStroke(Color.BLUE);
            gc.stroke();
        }
        else if (player == Player.O) {
            gc.setStroke(Color.RED);
            gc.strokeOval(0.2, 0.2, 0.6, 0.6);
        }

        gc.restore();
    }
}
