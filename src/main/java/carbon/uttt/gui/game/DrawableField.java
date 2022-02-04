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
    private boolean highlightEnabled = false;

    /**
     * Create a drawable wrapper for given field.
     * @param field Field to draw.
     */
    public DrawableField(Field field) {
        this.field = field;
    }

    /**
     * Highlight this field.
     * @param enabled Enable highlight.
     */
    public void highlight(boolean enabled) {
        highlightEnabled = enabled;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        gc.setLineWidth(0.1);

        if (highlightEnabled) {
            gc.setFill(Color.gray(0, 0.2));
            gc.fillRect(0, 0, 1, 1);
        }

        if (field.getFieldOwner() == Player.X) {
            gc.beginPath();
            gc.moveTo(0.3, 0.3);
            gc.lineTo(0.7, 0.7);
            gc.moveTo(0.7, 0.3);
            gc.lineTo(0.3, 0.7);

            gc.setStroke(Color.BLUE);
            gc.stroke();
        }
        else if (field.getFieldOwner() == Player.O) {
            gc.setStroke(Color.RED);
            gc.strokeOval(0.2, 0.2, 0.6, 0.6);
        }

        gc.restore();
    }
}
