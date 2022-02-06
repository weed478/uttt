package carbon.uttt.ai;

import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Pure Monte Carlo Tree Search.
 */
public class PMCTS implements IAI {

    private final PureGame game;

    private final Player player;

    /**
     * Number of samples to take per move.
     */
    private final int numSamples = 10;

    private final Random r = new Random();

    public PMCTS(PureGame game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public Pos9x9 decideMove() {
        // get all valid moves
        List<Pos9x9> moves = Pos9x9
                .values()
                .filter(game::moveValid)
                .collect(Collectors.toList());

        if (moves.isEmpty()) {
            throw new IllegalArgumentException("Cannot make a move");
        }

        if (moves.size() == 1) {
            return moves.get(0);
        }

        Collections.shuffle(moves);

        // get score for each possible move
        int[] scores = new int[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            int score = simulateGames(moves.get(i));
            scores[i] = score;
        }

        // choose move with best score
        int bestI = 0;
        int bestScore = scores[0];
        for (int i = 1; i < moves.size(); i++) {
            if (scores[i] > bestScore) {
                bestScore = scores[i];
                bestI = i;
            }
        }

        return moves.get(bestI);
    }

    private int simulateGames(Pos9x9 initialMove) {
        int score = 0;
        game.makeMove(initialMove);

        for (int i = 0; i < numSamples; i++) {
            score += rollout();
        }

        game.undoMove();
        return score;
    }

    private int rollout() {
        // simulate game till end
        int numMovesMade = 0;
        while (true) {
            // get valid moves
            List<Pos9x9> moves = Pos9x9
                    .values()
                    .filter(game::moveValid)
                    .collect(Collectors.toList());
            // is game is over?
            if (moves.isEmpty()) break;
            Collections.shuffle(moves);
            // make random move
            int i = r.nextInt(moves.size());
            game.makeMove(moves.get(i));
            numMovesMade += 1;
        }

        Player winner =  game.getWinner();

        // undo all moves
        for (int i = 0; i < numMovesMade; i++) {
            game.undoMove();
        }

        if (winner == player) {
            return 0;
        }
        if (winner == null) {
            return 0;
        }
        return -1;
    }
}
