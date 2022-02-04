package carbon.uttt.game;

/**
 * A board with a 3x3 grid
 * of local boards or single fields.
 */
public interface IBoard3x3 extends IField {

    /**
     * Get the field/local board at given position.
     * @param pos 3x3 position.
     * @return Field/local board.
     */
    IField getIField(Pos3x3 pos);
}
