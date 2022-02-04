package carbon.uttt.game;

/**
 * Represents a single field
 * or a local board that can act
 * as a field in a global board.
 */
public interface IField {

    /**
     * Get the field owner
     * (placed mark or winner of local board).
     * @return Player or null if no owner.
     */
    Player getFieldOwner();

    /**
     * Clear the placed mark or entire board.
     */
    void resetField();
}
