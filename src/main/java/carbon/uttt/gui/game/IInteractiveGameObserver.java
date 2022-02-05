package carbon.uttt.gui.game;

import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;

public interface IInteractiveGameObserver {

    void onGameNeedsRedraw(IInteractiveGame game);

    void onMoveMade(IInteractiveGame game, Player player, Pos9x9 pos);
}
