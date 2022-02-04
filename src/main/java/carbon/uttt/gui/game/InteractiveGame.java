package carbon.uttt.gui.game;

import carbon.uttt.game.*;
import carbon.uttt.gui.IDrawable;
import carbon.uttt.gui.IDrawableObserver;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Drawable, human interactive game.
 */
public class InteractiveGame extends Game implements IDrawable {

    private final Set<IDrawableObserver> drawableObservers = new HashSet<>();
    private final DrawableGlobalBoard drawableBoard = new DrawableGlobalBoard(getBoard());

    @Override
    public void reset() {
        super.reset();
        updateHighlights();
        notifyDrawableStale();
    }

    @Override
    public void makeMove(Pos9x9 move) {
        super.makeMove(move);
        updateHighlights();
        notifyDrawableStale();
    }

    @Override
    public Pos9x9 undoMove() {
        Pos9x9 move = super.undoMove();
        updateHighlights();
        notifyDrawableStale();
        return move;
    }

    /**
     * Reset highlights and generate
     * new for currently available moves.
     */
    private void updateHighlights() {
        highlightMoves(List.of());
        highlightLocalBoards(getAvailableLocalBoards());
    }

    /**
     * Highlight move if valid.
     * @param move 9x9 position.
     */
    public void highlightMove(Pos9x9 move) {
        if (moveValid(move)) {
            highlightMoves(List.of(move));
        }
        else {
            highlightMoves(List.of());
        }
        notifyDrawableStale();
    }

    /**
     * Enable highlight for given boards
     * and disable for the rest.
     * @param gps List of 3x3 positions.
     */
    private void highlightLocalBoards(List<Pos3x3> gps) {
        for (Pos3x3 gp : Pos3x3.values()) {
            drawableBoard.getDrawableLocalBoard(gp).highlight(false);
        }
        for (Pos3x3 gp : gps) {
            drawableBoard.getDrawableLocalBoard(gp).highlight(true);
        }
    }

    /**
     * Enable highlight for given fields
     * and disable for the rest.
     * @param moves List of 9x9 positions.
     */
    private void highlightMoves(List<Pos9x9> moves) {
        Pos9x9.values().forEach(p9x9 -> drawableBoard
                .getDrawableLocalBoard(p9x9.gp())
                .getDrawableField(p9x9.lp())
                .highlight(false)
        );
        for (Pos9x9 p9x9 : moves) {
            drawableBoard
                    .getDrawableLocalBoard(p9x9.gp())
                    .getDrawableField(p9x9.lp())
                    .highlight(true);
        }
    }

    /**
     * Get list of local board 3x3 positions
     * where moves can be made.
     * @return List of 3x3 positions.
     */
    private List<Pos3x3> getAvailableLocalBoards() {
        return Arrays.stream(Pos3x3.values())
                .filter(this::localBoardAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public void draw(GraphicsContext gc) {
        drawableBoard.draw(gc);
    }

    public void addDrawableObserver(IDrawableObserver observer) {
        drawableObservers.add(observer);
    }

    public void removeDrawableObserver(IDrawableObserver observer) {
        drawableObservers.remove(observer);
    }

    /**
     * Notify observers should redraw.
     */
    private void notifyDrawableStale() {
        for (IDrawableObserver o : drawableObservers) {
            o.onDrawableStale(this);
        }
    }
}