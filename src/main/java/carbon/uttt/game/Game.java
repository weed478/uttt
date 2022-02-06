package carbon.uttt.game;

import java.util.*;

public class Game implements IGame {

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
        if (!moveValid(move)) {
            throw new IllegalArgumentException("Illegal move: " + move);
        }
        board.setField(move, currentPlayer);
        moveHistory.add(move);
        currentPlayer = currentPlayer.nextPlayer();
    }

    @Override
    public Pos9x9 undoMove() {
        if (getLastMove() == null)
            throw new IllegalArgumentException("No moves to undo");

        board.setField(getLastMove(), null);
        currentPlayer = currentPlayer.nextPlayer();
        return moveHistory.remove(moveHistory.size() - 1);
    }

    @Override
    public boolean moveValid(Pos9x9 move) {
        /*
         * Move is valid when:
         * - is not null
         * - game is not won
         * - local board is available
         * - field is empty
         */
        return move != null &&
                board.getFieldOwner() == null &&
                localBoardAvailable(move.gp()) &&
                board.getLocalBoard(move.gp())
                        .getField(move.lp())
                        .getFieldOwner()
                        == null;
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
        return board.getFieldOwner() != null || Arrays.stream(Pos3x3.values()).noneMatch(this::localBoardAvailable);
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
         * - local board is not won
         * - local board has empty fields
         * - isFirstMove or GP == lastMove.LP or lastMove.LP is not available
         */
        return gp != null &&
                board.getLocalBoard(gp).getFieldOwner() == null &&
                Arrays.stream(Pos3x3.values())
                        .anyMatch(mp -> board
                                .getLocalBoard(gp)
                                .getField(mp)
                                .getFieldOwner()
                                == null) &&
                (isFirstMove() || gp == getLastMove().lp() || !localBoardAvailable(getLastMove().lp()));
    }
}
