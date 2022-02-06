package carbon.uttt.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple 3x3 board.
 */
public class LocalBoard extends AbstractBoard3x3 {

    private final List<List<Field>> cols;

    public LocalBoard() {
        cols = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Field> col = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Field f = new Field();
                col.add(f);
                f.addCachingObject(this);
            }
            cols.add(col);
        }
    }

    /**
     * Set a field on this board.
     * @param pos 3x3 position.
     * @param player Player or null to clear.
     */
    public void setField(Pos3x3 pos, Player player) {
        getField(pos).setPlayer(player);
    }

    /**
     * Get field on local board.
     * @param pos 3x3 field position.
     * @return field at position.
     */
    public Field getField(Pos3x3 pos) {
        return cols.get(pos.getX()).get(pos.getY());
    }

    @Override
    public IField getIField(Pos3x3 pos) {
        return getField(pos);
    }
}
