package carbon.uttt.ai;

import carbon.uttt.game.Pos9x9;

public interface IAI {

    /**
     * Decide which move to make
     * to advance current game.
     * @return Next move.
     */
    Pos9x9 decideMove();
}
