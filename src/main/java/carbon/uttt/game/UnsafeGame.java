package carbon.uttt.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Game class that does not check move validity.
 */
public class UnsafeGame implements IGame{

    private final GlobalBoard board = new GlobalBoard();
    private Player currentPlayer = Player.X;
    private final List<Pos9x9> moveHistory = new ArrayList<>();

    public Pos9x9 getLastMove() {
        return moveHistory.isEmpty() ? null : moveHistory.get(moveHistory.size() - 1);
    }

    @Override
    public void reset() {
        board.resetField();
        currentPlayer = Player.X;
        moveHistory.clear();
    }

    @Override
    public GlobalBoard getBoard() {
        return board;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void makeMove(Pos9x9 move) {
        board.setField(move, currentPlayer);
        moveHistory.add(move);
        currentPlayer = currentPlayer.nextPlayer();
    }

    @Override
    public Pos9x9 undoMove() {
        board.setField(getLastMove(), null);
        currentPlayer = currentPlayer.nextPlayer();
        return moveHistory.remove(moveHistory.size() - 1);
    }

    @Override
    public boolean moveValid(Pos9x9 move) {
        /*
         * Move is valid when:
         * - is not null
         * - field is empty
         * - local board is available
         * - game is not won
         */
        return move != null &&
                board.getLocalBoard(move.gp())
                        .getField(move.lp())
                        .getFieldOwner()
                        == null &&
                localBoardAvailable(move.gp()) &&
                board.getFieldOwner() == null;
    }

    @Override
    public boolean isFirstMove() {
        return moveHistory.isEmpty();
    }

    @Override
    public Player getWinner() {
        return board.getFieldOwner();
    }

    @Override
    public List<Pos9x9> getMoveHistory() {
        return moveHistory;
    }

    @Override
    public boolean isGameOver() {
        // winner or draw
        return getWinner() != null || Arrays.stream(Pos3x3.values()).noneMatch(this::localBoardAvailable);
    }

    /**
     * Check if any move can be made on local board.
     * @param gp 3x3 position of local board within global board.
     * @return True if local board is available.
     */
    public boolean localBoardAvailable(Pos3x3 gp) {
        /*
         * Board is available when:
         * - position is not null
         * - local board has empty fields
         * - local board is not won
         * - isFirstMove or GP == lastMove.LP or lastMove.LP is not available
         */
        return gp != null &&
                Arrays.stream(Pos3x3.values())
                        .anyMatch(mp -> board
                                .getLocalBoard(gp)
                                .getField(mp)
                                .getFieldOwner()
                                == null) &&
                board.getLocalBoard(gp).getFieldOwner() == null &&
                (isFirstMove() || gp == getLastMove().lp() || !localBoardAvailable(getLastMove().lp()));
    }
}
