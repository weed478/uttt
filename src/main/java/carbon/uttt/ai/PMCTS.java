package carbon.uttt.ai;

import carbon.uttt.game.Game;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Pure Monte Carlo Tree Search.
 */
public class PMCTS implements IAI {

    private final int T = 10;
    private final Game game;

    public PMCTS(Game game) {
        this.game = game;
    }

    @Override
    public Pos9x9 decideMove() {
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

        int[] scores = new int[moves.size()];

        for (int i = 0; i < moves.size(); i++) {
            int score = simulateGames(moves.get(i));
            scores[i] = score;
        }

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
        System.out.println("Playing " + initialMove);

        int score = 0;
        game.makeMove(initialMove);

        for (int i = 0; i < T; i++) {
            score += rollout();
        }

        game.undoMove();
        return score;
    }

    private int rollout() {
        Random r = new Random();
        int numMovesMade = 0;
        while (true) {
            List<Pos9x9> moves = Pos9x9
                    .values()
                    .filter(game::moveValid)
                    .collect(Collectors.toList());
            if (moves.isEmpty()) break;
            int i = r.nextInt(moves.size());
            game.makeMove(moves.get(i));
            numMovesMade += 1;
        }

        for (int i = 0; i < numMovesMade; i++) {
            game.undoMove();
        }

        Player winner =  game.getBoard().getFieldOwner();
        System.out.println(winner);
        return winner == Player.O ? 0 : 1;
    }
}
