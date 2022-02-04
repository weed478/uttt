package carbon.uttt.gui.game;

import carbon.uttt.game.LocalBoard;
import carbon.uttt.game.Pos3x3;
import carbon.uttt.gui.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Drawable wrapper for a 3x3 local board.
 */
public class DrawableLocalBoard extends AbstractDrawableBoard3x3 {

    private final List<List<DrawableField>> cols;
    private boolean highlightEnabled = false;

    /**
     * Create a wrapper for drawing given local board.
     * @param board Local board to draw.
     */
    public DrawableLocalBoard(LocalBoard board) {
        cols = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            List<DrawableField> col = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                col.add(new DrawableField(board.getField(Pos3x3.fromXY(x, y))));
            }
            cols.add(col);
        }
    }

    /**
     * Highlight whole local board.
     * @param enabled Enable highlight.
     */
    public void highlight(boolean enabled) {
        highlightEnabled = enabled;
    }

    /**
     * Get drawable field wrapper instance at position.
     * @param pos 3x3 position.
     * @return Drawable field wrapper.
     */
    public DrawableField getDrawableField(Pos3x3 pos) {
        return cols.get(pos.getX()).get(pos.getY());
    }

    @Override
    protected IDrawable getDrawableAt(Pos3x3 pos) {
        return getDrawableField(pos);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        gc.translate(0.05, 0.05);
        gc.scale(0.9, 0.9);

        if (highlightEnabled) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(0.03);
            gc.strokeRoundRect(0, 0, 1, 1, 0.1, 0.1);
        }

        gc.translate(0.05, 0.05);
        gc.scale(0.9, 0.9);

        gc.beginPath();

        gc.moveTo(0.33, 0);
        gc.lineTo(0.33, 1);
        gc.moveTo(0.66, 0);
        gc.lineTo(0.66, 1);

        gc.moveTo(0, 0.33);
        gc.lineTo(1, 0.33);
        gc.moveTo(0, 0.66);
        gc.lineTo(1, 0.66);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.01);
        gc.stroke();

        super.draw(gc);

        gc.restore();
    }
}
