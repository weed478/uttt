package carbon.uttt.ai;

import carbon.uttt.game.IGame;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Pure Monte Carlo Tree Search.
 * Uses all CPU cores.
 */
public class PMCTS implements IAI {

    private final IGame game;

    private final Player player;

    private final long computationTimeoutMs;

    private boolean keepRunning = true;

    public PMCTS(IGame game, Player player, long computationTimeoutMs) {
        this.game = game;
        this.player = player;
        this.computationTimeoutMs = computationTimeoutMs;
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

        int numThreads = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = new ArrayList<>(numThreads);
        int[][] scoresTh = new int[numThreads][moves.size()];

        keepRunning = true;

        for (int t = 0; t < numThreads; t++) {
            int tNum = t;
            Thread th = new Thread(() -> {
                // thread local game copy
                PureGame myGame = new PureGame(game);
                while (true) {
                    synchronized (this) {
                        if (!keepRunning) break;
                    }
                    // get score for each possible move
                    for (int i = 0; i < moves.size(); i++) {
                        scoresTh[tNum][i] += rollout(myGame, player, moves.get(i));
                    }
                }
            });
            th.setDaemon(true);
            th.start();
            threads.add(th);
        }

        try {
            Thread.sleep(computationTimeoutMs);
        } catch (InterruptedException ignored) {}

        synchronized (this) {
            keepRunning = false;
        }

        // wait for threads
        for (int t = 0; t < threads.size(); t++) {
            try {
                threads.get(t).join();
            } catch (InterruptedException e) {
                // retry joining
                t--;
            }
        }

        // reduce scores
        int[] scores = new int[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            for (int t = 0; t < numThreads; t++) {
                scores[i] += scoresTh[t][i];
            }
        }

        for (int i : scores) {
            System.out.print(i + " ");
        }
        System.out.println();

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

    private static int rollout(PureGame game, Player player, Pos9x9 initialMove) {
        Random r = new Random();
        game.makeMove(initialMove);
        int numMovesMade = 1;
        // simulate game till end
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

        // score based on result
        if (winner == player) {
            return 1;
        }
        if (winner == null) {
            return 0;
        }
        // punish losing
        return -2;
    }
}
