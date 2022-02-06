package carbon.uttt.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Universal board implementation.
 */
public abstract class AbstractBoard3x3 implements IBoard3x3, ICachingObject {

    private boolean fieldOwnerCacheValid = false;

    private Player fieldOwnerCache;

    private final Set<ICachingObject> cachingObjects = new HashSet<>();

    /**
     * Add object that caches a computation based on this board.
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
     * this board's winner has changed.
     */
    private void notifyCacheInvalid() {
        for (ICachingObject o : cachingObjects) {
            o.invalidateCache();
        }
    }

    /**
     * Reset this board.
     */
    @Override
    public void resetField() {
        for (Pos3x3 p : Pos3x3.values()) {
            getIField(p).resetField();
        }
    }

    /**
     * Get player who won this board.
     * @return Winner or null.
     */
    @Override
    public Player getFieldOwner() {
        if (fieldOwnerCacheValid) return fieldOwnerCache;
        Player oldOwner = fieldOwnerCache;
        fieldOwnerCache = calculateFieldOwner();
        fieldOwnerCacheValid = true;
        if (fieldOwnerCache != oldOwner) {
            notifyCacheInvalid();
        }
        return fieldOwnerCache;
    }

    @Override
    public void invalidateCache() {
        fieldOwnerCacheValid = false;
    }

    private Player calculateFieldOwner() {
        // find 3 in a row

        // vertical
        for (int x = 0; x < 3; x++) {
            Player player = getIField(Pos3x3.fromXY(x, 0)).getFieldOwner();
            for (int y = 1; y < 3; y++) {
                Pos3x3 pos = Pos3x3.fromXY(x, y);
                if (getIField(pos).getFieldOwner() != player) {
                    player = null;
                    break;
                }
            }
            if (player != null) return player;
        }

        // horizontal
        for (int y = 0; y < 3; y++) {
            Player player = getIField(Pos3x3.fromXY(0, y)).getFieldOwner();
            for (int x = 1; x < 3; x++) {
                Pos3x3 pos = Pos3x3.fromXY(x, y);
                if (getIField(pos).getFieldOwner() != player) {
                    player = null;
                    break;
                }
            }
            if (player != null) return player;
        }

        // diagonal \
        {
            Player player = getIField(Pos3x3.fromXY(0, 0)).getFieldOwner();
            for (int xy = 1; xy < 3; xy++) {
                Pos3x3 pos = Pos3x3.fromXY(xy, xy);
                if (getIField(pos).getFieldOwner() != player) {
                    player = null;
                    break;
                }
            }
            if (player != null) return player;
        }

        // diagonal /
        {
            Player player = getIField(Pos3x3.fromXY(0, 2)).getFieldOwner();
            for (int xy = 1; xy < 3; xy++) {
                Pos3x3 pos = Pos3x3.fromXY(xy, 2 - xy);
                if (getIField(pos).getFieldOwner() != player) {
                    player = null;
                    break;
                }
            }
            if (player != null) return player;
        }

        return null;
    }
}
