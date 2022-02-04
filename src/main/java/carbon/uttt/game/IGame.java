package carbon.uttt.game;

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
}
