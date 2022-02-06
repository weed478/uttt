package carbon.uttt.gui.game;

import carbon.uttt.game.IGame;
import carbon.uttt.game.Pos9x9;
import carbon.uttt.gui.IDrawable;

public interface IInteractiveGame extends IGame, IDrawable {

    void addGameObserver(IInteractiveGameObserver o);

    void removeGameObserver(IInteractiveGameObserver o);

    /**
     * Highlight move if valid.
     * @param move 9x9 position.
     */
    void highlightMove(Pos9x9 move);

    /**
     * Does the game have 2 human players (or v. AI).
     */
    boolean isTwoPlayer();
}
