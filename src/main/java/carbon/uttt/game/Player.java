package carbon.uttt.game;

public enum Player {
    X,
    O,
    EMPTY;

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public Player nextPlayer() {
        switch (this) {
            case X: return O;
            case O: return X;
            default:
                throw new IllegalArgumentException("Player.EMPTY has no next");
        }
    }
}
