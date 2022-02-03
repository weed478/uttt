package carbon.uttt.game;

import carbon.uttt.gui.IDrawable;
import carbon.uttt.gui.IDrawableObserver;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;
import java.util.stream.Collectors;

public class Game implements IDrawable {

    private final Set<IDrawableObserver> drawableObservers = new HashSet<>();
    private final BoardDP board = new BoardDP();
    private Player currentPlayer = Player.EMPTY;
    private final List<PosDPMP> moveHistory = new ArrayList<>();

    public void newGame() {
        board.reset();
        currentPlayer = Player.X;
        moveHistory.clear();
        highlightAvailableBoardsDP();
        notifyDrawableStale();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void makeMove(PosDPMP dpmp) {
        if (!moveValid(dpmp)) {
            throw new IllegalArgumentException("Illegal move: " + dpmp);
        }
        board.getBoardMP(dpmp.dp())
                .getField(dpmp.mp())
                .setPlayer(currentPlayer);
        moveHistory.add(dpmp);
        currentPlayer = currentPlayer.nextPlayer();
        highlightAvailableBoardsDP();
        notifyDrawableStale();
    }

    private void highlightAvailableBoardsDP() {
        if (isFirstMove()) {
            highlightBoardsDP(Set.of(Pos3x3.values()));
        }
        else {
            PosDPMP lastMove = moveHistory.get(moveHistory.size() - 1);
            if (moveValidDP(lastMove.mp())) {
                highlightBoardsDP(Set.of(lastMove.mp()));
            }
            else {
                highlightBoardsDP(Arrays.stream(Pos3x3.values())
                        .filter(this::moveValidDP)
                        .collect(Collectors.toSet()));
            }
        }
    }

    private boolean moveValidDP(Pos3x3 dp) {
        return board.getBoardMP(dp).getPlayer().isEmpty() &&
                Arrays.stream(Pos3x3.values())
                        .anyMatch(mp -> board
                                .getBoardMP(dp)
                                .getField(mp)
                                .getPlayer()
                                .isEmpty());
    }

    private boolean isFirstMove() {
        return moveHistory.isEmpty();
    }

    private boolean moveValid(PosDPMP dpmp) {
        BoardMP bmp = board.getBoardMP(dpmp.dp());
        return bmp.getPlayer().isEmpty() && bmp.getField(dpmp.mp()).getPlayer().isEmpty();
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
