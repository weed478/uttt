package carbon.uttt.game;

/**
 * Single field on a local board.
 */
public class Field implements IField {

    private Player player = null;

    /**
     * Place a player on this field.
     * @param p Player to place or null to clear.
     */
    public void setPlayer(Player p) {
        player = p;
    }

    /**
     * Get player placed on this field.
     * @return Player or null.
     */
    @Override
    public Player getFieldOwner() {
        return player;
    }

    /**
     * Reset this field.
     */
    @Override
    public void resetField() {
        player = null;
    }
}
