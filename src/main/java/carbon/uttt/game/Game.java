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
        highlightAvailableMovesDP();
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
        highlightAvailableMovesDP();
        notifyDrawableStale();
    }

    public void highlightMove(PosDPMP dpmp) {
        if (dpmp == null) {
            highlightMovesDPMP(Set.of());
        }
        else if (moveValid(dpmp)) {
            highlightMovesDPMP(Set.of(dpmp));
        }
        notifyDrawableStale();
    }

    private void highlightAvailableMovesDP() {
        if (isFirstMove()) {
            highlightMovesDP(Set.of(Pos3x3.values()));
        }
        else {
            PosDPMP lastMove = moveHistory.get(moveHistory.size() - 1);
            if (moveValidDP(lastMove.mp())) {
                highlightMovesDP(Set.of(lastMove.mp()));
            }
            else {
                highlightMovesDP(Arrays.stream(Pos3x3.values())
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
