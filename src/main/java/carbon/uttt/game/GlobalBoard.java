package carbon.uttt.game;

import java.util.ArrayList;
import java.util.List;

public class GlobalBoard extends AbstractBoard3x3 {

    private final List<List<LocalBoard>> cols;

    public GlobalBoard() {
        cols = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<LocalBoard> col = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                col.add(new LocalBoard());
            }
            cols.add(col);
        }
    }

    /**
     * Set field at 9x9 position.
     * @param pos 9x9 position.
     * @param player Player or null to clear field.
     */
    public void setField(Pos9x9 pos, Player player) {
        getLocalBoard(pos.gp()).setField(pos.lp(), player);
    }

    /**
     * Get local board on global board.
     * @param pos 3x3 position of local board.
     * @return local board at position.
     */
    public LocalBoard getLocalBoard(Pos3x3 pos) {
        return cols.get(pos.getX()).get(pos.getY());
    }

    @Override
    public IField getIField(Pos3x3 pos) {
        return getLocalBoard(pos);
    }
}
