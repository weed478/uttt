package carbon.uttt.gui.game;

import carbon.uttt.ai.IAI;
import carbon.uttt.ai.RandomAI;
import carbon.uttt.game.Pos9x9;

public class PvAIGame extends InteractiveGame {

    private final IAI ai = new RandomAI(game);

    @Override
    public void makeMove(Pos9x9 move) {
        super.makeMove(move);
        super.makeMove(ai.decideMove());
    }
}
