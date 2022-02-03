package carbon.uttt.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class BoardMP extends AbstractBoard3x3 {

    private boolean enableHighlight = false;
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

    public void enableHighlight(boolean enabled) {
        enableHighlight = enabled;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        gc.translate(0.05, 0.05);
        gc.scale(0.9, 0.9);

        if (enableHighlight) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(0.03);
            gc.strokeRoundRect(0, 0, 1, 1, 0.1, 0.1);
        }

        gc.translate(0.05, 0.05);
        gc.scale(0.9, 0.9);

        gc.beginPath();

        gc.moveTo(0.33, 0);
        gc.lineTo(0.33, 1);
        gc.moveTo(0.66, 0);
        gc.lineTo(0.66, 1);

        gc.moveTo(0, 0.33);
        gc.lineTo(1, 0.33);
        gc.moveTo(0, 0.66);
        gc.lineTo(1, 0.66);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.01);
        gc.stroke();

        super.draw(gc);

        gc.restore();
    }

    @Override
    public void reset() {
        enableHighlight = false;
        super.reset();
    }
}
