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
        highlightMovesDPMP(Set.of());
        highlightMovesDP(availableMovesDP());
        notifyDrawableStale();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void makeMoveDPMP(PosDPMP dpmp) {
        if (!moveValidDPMP(dpmp)) {
            throw new IllegalArgumentException("Illegal move: " + dpmp);
        }
        board.getBoardMP(dpmp.dp())
                .getField(dpmp.mp())
                .setPlayer(currentPlayer);
        moveHistory.add(dpmp);
        currentPlayer = currentPlayer.nextPlayer();
        highlightMovesDPMP(Set.of());
        highlightMovesDP(availableMovesDP());
        notifyDrawableStale();
    }

    public void highlightMoveDPMP(PosDPMP dpmp) {
        if (moveValidDPMP(dpmp)) {
            highlightMovesDPMP(Set.of(dpmp));
        }
        else {
            highlightMovesDPMP(Set.of());
        }
        notifyDrawableStale();
    }

    /**
     * Checks if DP move is valid.
     * Move is valid when:
     * - is not null
     * - DP is not won
     * - DP has empty fields
     * - isFirstMove or DP == lastMove.MP or !isValid(DP(lastMove.MP))
     */
    private boolean moveValidDP(Pos3x3 dp) {
        return dp != null &&
                board.getBoardMP(dp).getPlayer().isEmpty() &&
                Arrays.stream(Pos3x3.values())
                        .anyMatch(mp -> board
                                .getBoardMP(dp)
                                .getField(mp)
                                .getPlayer()
                                .isEmpty()) &&
                (isFirstMove() || dp == lastMove().mp() || !moveValidDP(lastMove().mp()));
    }

    /**
     * Checks if DPMP move is valid.
     * Move is valid when:
     * - is not null
     * - DP move is valid
     * - MP is empty
     */
    private boolean moveValidDPMP(PosDPMP dpmp) {
        return dpmp != null &&
                moveValidDP(dpmp.dp()) &&
                board.getBoardMP(dpmp.dp())
                        .getField(dpmp.mp())
                        .getPlayer()
                        .isEmpty();
    }

    private void highlightMovesDP(Set<Pos3x3> dps) {
        for (Pos3x3 p : Pos3x3.values()) {
            board.getBoardMP(p).highlight(dps.contains(p));
        }
    }

    private void highlightMovesDPMP(Set<PosDPMP> dpmps) {
        for (Pos3x3 dp : Pos3x3.values()) {
            for (Pos3x3 mp : Pos3x3.values()) {
                board.getBoardMP(dp)
                        .getField(mp)
                        .highlight(dpmps.contains(new PosDPMP(dp, mp)));
            }
        }
    }

    private Set<Pos3x3> availableMovesDP() {
        return Arrays.stream(Pos3x3.values())
                .filter(this::moveValidDP)
                .collect(Collectors.toSet());
    }

    private PosDPMP lastMove() {
        return moveHistory.isEmpty() ? null : moveHistory.get(moveHistory.size() - 1);
    }

    private boolean isFirstMove() {
        return moveHistory.isEmpty();
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
