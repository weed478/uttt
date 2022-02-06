package carbon.uttt.ai;

import carbon.uttt.game.IGame;
import carbon.uttt.game.Pos9x9;
import carbon.uttt.game.UnsafeGame;

/**
 * A game without any side effects like UI rendering.
 * Used for simulating AI moves.
 */
public class PureGame extends UnsafeGame {

    /**
     * Create a pure game. Creates a copy.
     * @param game Game to convert into pure.
     */
    public PureGame(IGame game) {
        for (Pos9x9 move : game.getMoveHistory()) {
            makeMove(move);
        }
    }
}
