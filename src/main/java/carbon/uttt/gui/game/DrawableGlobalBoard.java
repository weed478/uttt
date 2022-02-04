package carbon.uttt.gui.game;

import carbon.uttt.game.GlobalBoard;
import carbon.uttt.game.Pos3x3;
import carbon.uttt.gui.IDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Drawable global board wrapper.
 */
public class DrawableGlobalBoard extends AbstractDrawableBoard3x3 {

    private final List<List<DrawableLocalBoard>> cols;

    /**
     * Create a wrapper for drawing given global board.
     * @param board Global board to draw.
     */
    public DrawableGlobalBoard(GlobalBoard board) {
        super(board);
        cols = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            List<DrawableLocalBoard> col = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                col.add(new DrawableLocalBoard(board.getLocalBoard(Pos3x3.fromXY(x, y))));
            }
            cols.add(col);
        }
    }

    /**
     * Get drawable local board wrapper at position.
     * @param pos 3x3 position.
     * @return Drawable local board wrapper.
     */
    public DrawableLocalBoard getDrawableLocalBoard(Pos3x3 pos) {
        return cols.get(pos.getX()).get(pos.getY());
    }

    @Override
    protected IDrawable getDrawableAt(Pos3x3 pos) {
        return getDrawableLocalBoard(pos);
    }
}
