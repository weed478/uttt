package carbon.uttt.game;

import java.util.ArrayList;
import java.util.List;

public class BoardDP extends AbstractBoard3x3 {

    private final List<List<BoardMP>> cols;

    public BoardDP() {
        cols = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<BoardMP> col = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                col.add(new BoardMP());
            }
            cols.add(col);
        }
    }

    public BoardMP getBoardMP(Pos3x3 pos) {
        return cols.get(pos.getX()).get(pos.getY());
    }

    @Override
    public IField getIField(Pos3x3 pos) {
        return getBoardMP(pos);
    }

    @Override
    public Player getPlayer() {
        return Player.EMPTY;
    }
}
