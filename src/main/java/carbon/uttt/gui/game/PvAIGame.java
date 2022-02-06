package carbon.uttt.gui.game;

import carbon.uttt.ai.IAI;
import carbon.uttt.ai.PMCTS;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;

/**
 * Player v. AI game session.
 */
public class PvAIGame extends PvPGame {

    private final long computationTimeoutMs = 4000;

    private final IAI ai = new PMCTS(this, Player.O, computationTimeoutMs);

    @Override
    public void makeMove(Pos9x9 move) {
        super.makeMove(move);
        if (!isGameOver()) {
            Thread aiThread = new Thread(() -> super.makeMove(ai.decideMove()));
            aiThread.setDaemon(true);
            aiThread.start();
        }
    }

    @Override
    public boolean isTwoPlayer() {
        return false;
    }
}
