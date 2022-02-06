package carbon.uttt.gui.game;

import carbon.uttt.ai.PMCTS;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Player v. AI game session.
 */
public class PvAIGame extends PvPGame {

    private final long computationTimeoutMs = 4000;

    @Override
    public void makeMove(Pos9x9 move) {
        super.makeMove(move);
        if (!isGameOver()) {
            Thread aiThread = new Thread(() -> {
                PMCTS ai = new PMCTS(this, Player.O);
                Timer computationTimer = new Timer(true);
                computationTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ai.stopComputation();
                    }
                }, computationTimeoutMs);
                super.makeMove(ai.decideMove());
            });
            aiThread.setDaemon(true);
            aiThread.start();
        }
    }

    @Override
    public boolean isTwoPlayer() {
        return false;
    }
}
