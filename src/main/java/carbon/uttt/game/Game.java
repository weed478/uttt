package carbon.uttt.game;

/**
 * Game class that checks move validity.
 */
public class Game extends UnsafeGame {

    @Override
    public void makeMove(Pos9x9 move) {
        if (!moveValid(move)) {
            throw new IllegalArgumentException("Illegal move: " + move);
        }
        super.makeMove(move);
    }

    @Override
    public Pos9x9 undoMove() {
        if (getLastMove() == null)
            throw new IllegalArgumentException("No moves to undo");
        return super.undoMove();
    }
}
