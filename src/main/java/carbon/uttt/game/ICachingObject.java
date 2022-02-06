package carbon.uttt.game;

/**
 * Object that caches a computation based on a dependency.
 * The dependency can invalidate the cache.
 */
public interface ICachingObject {

    void invalidateCache();
}
