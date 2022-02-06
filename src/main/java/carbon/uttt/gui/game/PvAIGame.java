package carbon.uttt.gui.game;

import carbon.uttt.ai.IAI;
import carbon.uttt.ai.PMCTS;
import carbon.uttt.ai.PureGame;
import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;

/**
 * Player v. AI game session.
 */
public class PvAIGame extends PvPGame {

    @Override
    public void makeMove(Pos9x9 move) {
        super.makeMove(move);
        if (!isGameOver()) {
            IAI ai = new PMCTS(new PureGame(this), Player.O);
            super.makeMove(ai.decideMove());
        }
    }
}
