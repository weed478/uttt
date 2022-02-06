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

    /**
     * Add object that caches a computation based on this field.
     */
    public void addCachingObject(ICachingObject o) {
        cachingObjects.add(o);
    }

    /**
     * Remove caching object.
     */
    public void removeCachingObject(ICachingObject o) {
        cachingObjects.remove(o);
    }

    /**
     * Notify all subscribed objects that they
     * need to recompute their cache because
     * this field has changed.
     */
    private void notifyCacheInvalid() {
        for (ICachingObject o : cachingObjects) {
            o.invalidateCache();
        }
    }
}
