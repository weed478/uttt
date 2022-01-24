package carbon.uttt.game;

import java.util.ArrayList;
import java.util.List;

public class BoardMP extends AbstractBoard3x3 {

    private final List<List<Field>> cols;

    public BoardMP() {
        cols = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Field> col = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                col.add(new Field());
            }
            cols.add(col);
        }
    }

    public Field getField(Pos3x3 pos) {
        return cols.get(pos.getX()).get(pos.getY());
    }

    @Override
    public IField getIField(Pos3x3 pos) {
        return getField(pos);
    }

    @Override
    public Player getPlayer() {
        return Player.EMPTY;
    }

    public void putPlayer(Pos3x3 pos, Player player) {
        getField(pos).setPlayer(player);
    }
}
