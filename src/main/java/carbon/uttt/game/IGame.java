package carbon.uttt.game;

import java.util.List;

public interface IGame {

    /**
     * Reset game state.
     */
    void reset();

    /**
     * @return global board.
     */
    GlobalBoard getBoard();

    /**
     * Get the player who makes next move.
     * @return Current player.
     */
    Player getCurrentPlayer();

    /**
     * Execute move as current player.
     * @param move 9x9 position where move will be made.
     */
    void makeMove(Pos9x9 move);

    /**
     * Undo effects of last move.
     * @return Last move.
     */
    Pos9x9 undoMove();

    /**
     * Check if 9x9 move is valid.
     * @param move 9x9 position.
     * @return True if move is valid.
     */
    boolean moveValid(Pos9x9 move);

    /**
     * Returns whether the next move will be the first
     */
    boolean isFirstMove();

    /**
     * Returns the winner of the game.
     * @return Winner or null.
     */
    Player getWinner();

    /**
     * Returns a list of all moves made so far (in order).
     */
    List<Pos9x9> getMoveHistory();
}
