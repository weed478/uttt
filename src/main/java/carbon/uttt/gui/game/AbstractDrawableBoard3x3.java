package carbon.uttt.gui.game;

import carbon.uttt.game.Pos3x3;
import carbon.uttt.gui.IDrawable;
import javafx.scene.canvas.GraphicsContext;

/**
 * Drawable wrapper for a 3x3 board.
 */
public abstract class AbstractDrawableBoard3x3 implements IDrawable {

    /**
     * Get a drawable field/local board at 3x3 position
     * within this board.
     * @param pos 3x3 position.
     * @return Drawable object.
     */
    protected abstract IDrawable getDrawableAt(Pos3x3 pos);

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.scale(1./3, 1./3);

        for (Pos3x3 pos : Pos3x3.values()) {
            gc.save();
            gc.translate(pos.getX(), pos.getY());
            IDrawable field = getDrawableAt(pos);
            field.draw(gc);
            gc.restore();
        }

        gc.restore();
    }
}
