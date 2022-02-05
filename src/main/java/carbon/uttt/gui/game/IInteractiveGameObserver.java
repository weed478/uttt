package carbon.uttt.gui.game;

import carbon.uttt.game.Player;
import carbon.uttt.game.Pos9x9;

/**
 * Observes an interactive game session.
 */
public interface IInteractiveGameObserver {

    /**
     * Called when board state changes.
     * @param game Game instance where event happened.
     */
    void onGameNeedsRedraw(IInteractiveGame game);

    /**
     * Called when a player makes a move.
     * @param game Game instance where event happened.
     * @param player Player who made the move.
     * @param pos Move position.
     */
    void onMoveMade(IInteractiveGame game, Player player, Pos9x9 pos);

    /**
     * Called when next turn begins.
     * @param game Game instance where event happened.
     * @param newPlayer Player whose turn it is now.
     */
    void onCurrentPlayerChanged(IInteractiveGame game, Player newPlayer);
}
