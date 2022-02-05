package carbon.uttt.gui.game;

import carbon.uttt.game.Field;
import carbon.uttt.game.Player;
import carbon.uttt.gui.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Drawable wrapper for a field.
 */
public class DrawableField implements IDrawable {

    private final Field field;
    private Player highlightedPlayer = null;

    /**
     * Create a drawable wrapper for given field.
     * @param field Field to draw.
     */
    public DrawableField(Field field) {
        this.field = field;
    }

    /**
     * Show which player is about to make a move.
     * @param player Player or null to disable highlight.
     */
    public void highlight(Player player) {
        highlightedPlayer = player;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        gc.setLineWidth(0.1);

        Player player;

        if (highlightedPlayer != null) {
            player = highlightedPlayer;
            gc.setGlobalAlpha(0.3);
        }
        else {
            player = field.getFieldOwner();
        }

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

        gc.setGlobalAlpha(1);

        gc.restore();
    }
}
