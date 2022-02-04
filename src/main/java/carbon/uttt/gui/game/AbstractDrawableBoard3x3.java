package carbon.uttt.gui.game;

import carbon.uttt.game.IBoard3x3;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos3x3;
import carbon.uttt.gui.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Drawable wrapper for a 3x3 board.
 */
public abstract class AbstractDrawableBoard3x3 implements IDrawable {

    private final IBoard3x3 board;

    public AbstractDrawableBoard3x3(IBoard3x3 board) {
        this.board = board;
    }

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

        if (board.getFieldOwner() != null)
            gc.setGlobalAlpha(0.2);

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

        if (board.getFieldOwner() != null)
            gc.setGlobalAlpha(1);

        if (board.getFieldOwner() == Player.X) {
            gc.beginPath();
            gc.moveTo(0.3, 0.3);
            gc.lineTo(0.7, 0.7);
            gc.moveTo(0.7, 0.3);
            gc.lineTo(0.3, 0.7);
            gc.setLineWidth(0.1);
            gc.setStroke(Color.BLUE);
            gc.stroke();
        }
        else if (board.getFieldOwner() == Player.O) {
            gc.setLineWidth(0.1);
            gc.setStroke(Color.RED);
            gc.strokeOval(0.2, 0.2, 0.6, 0.6);
        }

        gc.restore();
    }
}
