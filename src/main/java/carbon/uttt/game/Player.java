package carbon.uttt.game;

/**
 * X or O player.
 */
public enum Player {
    X,
    O;

    /**
     * Gets the player who has the next turn.
     * @return Next player.
     */
    public Player nextPlayer() {
        switch (this) {
            case X: return O;
            case O: return X;
            default:
                throw new IllegalArgumentException("Player " + this + " has no next");
        }
    }
}
