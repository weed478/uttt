package carbon.uttt.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Single field on a local board.
 */
public class Field implements IField {

    private Player player = null;

    private final Set<ICachingObject> cachingObjects = new HashSet<>();

    /**
     * Place a player on this field.
     * @param p Player to place or null to clear.
     */
    public void setPlayer(Player p) {
        if (p != player) {
            player = p;
            notifyCacheInvalid();
        }
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
        setPlayer(null);
    }

    public void addCachingObject(ICachingObject o) {
        cachingObjects.add(o);
    }

    public void removeCachingObject(ICachingObject o) {
        cachingObjects.remove(o);
    }

    private void notifyCacheInvalid() {
        for (ICachingObject o : cachingObjects) {
            o.invalidateCache();
        }
    }
}
