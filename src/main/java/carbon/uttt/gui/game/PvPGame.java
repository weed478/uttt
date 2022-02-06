package carbon.uttt.gui.game;

import carbon.uttt.game.*;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Player v. Player game session.
 */
public class PvPGame extends Game implements IInteractiveGame {

    private final Set<IInteractiveGameObserver> observers = new HashSet<>();
    private final DrawableGlobalBoard drawableBoard = new DrawableGlobalBoard(getBoard());

    @Override
    public void reset() {
        super.reset();
        updateHighlights();
        notifyDrawableStale();
        notifyPlayerChanged(getCurrentPlayer());
    }

    @Override
    public void makeMove(Pos9x9 move) {
        Pos9x9 lastMove = getLastMove();
        Player p = getCurrentPlayer();
        super.makeMove(move);

        // update last move highlight
        if (lastMove != null) {
            drawableBoard.getDrawableLocalBoard(lastMove.gp())
                    .getDrawableField(lastMove.lp())
                    .setIsLatestMove(false);
        }
        drawableBoard.getDrawableLocalBoard(move.gp())
                .getDrawableField(move.lp())
                .setIsLatestMove(true);

        updateHighlights();
        notifyDrawableStale();
        notifyMoveMade(p, move);
        notifyPlayerChanged(getCurrentPlayer());
    }

    @Override
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
     * Reset highlights and generate
     * new for currently available moves.
     */
    private void updateHighlights() {
        highlightMoves(List.of());
        highlightLocalBoards(getAvailableLocalBoards());
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
                .highlight(null)
        );
        for (Pos9x9 p9x9 : moves) {
            drawableBoard
                    .getDrawableLocalBoard(p9x9.gp())
                    .getDrawableField(p9x9.lp())
                    .highlight(getCurrentPlayer());
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

    @Override
    public void addGameObserver(IInteractiveGameObserver o) {
        observers.add(o);
    }

    @Override
    public void removeGameObserver(IInteractiveGameObserver o) {
        observers.remove(o);
    }

    /**
     * Notify observers should redraw.
     */
    private void notifyDrawableStale() {
        for (IInteractiveGameObserver o : observers) {
            o.onGameNeedsRedraw(this);
        }
    }

    private void notifyMoveMade(Player player, Pos9x9 move) {
        for (IInteractiveGameObserver o : observers) {
            o.onMoveMade(this, player, move);
        }
    }

    private void notifyPlayerChanged(Player newPlayer) {
        for (IInteractiveGameObserver o : observers) {
            o.onCurrentPlayerChanged(this, newPlayer);
        }
    }
}
