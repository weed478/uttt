package carbon.uttt.game;

import carbon.uttt.gui.IDrawable;
import carbon.uttt.gui.IDrawableObserver;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Set;

public class Game implements IDrawable {

    private final Set<IDrawableObserver> drawableObservers = new HashSet<>();
    private final BoardDP board = new BoardDP();

    public void newGame() {
        board.reset();
        highlightBoardsDP(Set.of(Pos3x3.values()));
        notifyDrawableStale();
    }

    private void highlightBoardsDP(Set<Pos3x3> dps) {
        for (Pos3x3 p : Pos3x3.values()) {
            board.getBoardMP(p).enableHighlight(dps.contains(p));
        }
    }

    public void addDrawableObserver(IDrawableObserver observer) {
        drawableObservers.add(observer);
    }

    public void removeDrawableObserver(IDrawableObserver observer) {
        drawableObservers.remove(observer);
    }

    private void notifyDrawableStale() {
        for (IDrawableObserver o : drawableObservers) {
            o.onDrawableStale(this);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        board.draw(gc);
    }
}
