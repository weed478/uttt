package carbon.uttt.gui.game;

import carbon.uttt.ai.IAI;
import carbon.uttt.ai.RandomAI;
import carbon.uttt.game.Pos9x9;

/**
 * Player v. AI game session.
 */
public class PvAIGame extends PvPGame {

    private final IAI ai = new RandomAI(this);

    @Override
    public void makeMove(Pos9x9 move) {
        super.makeMove(move);
        if (getWinner() == null)
            super.makeMove(ai.decideMove());
    }
}
