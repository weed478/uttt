package carbon.uttt.game;

import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractBoard3x3 implements IBoard3x3 {

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.scale(1./3, 1./3);

        for (Pos3x3 pos : Pos3x3.values()) {
            gc.save();
            gc.translate(pos.getX(), pos.getY());
            IField field = getIField(pos);
            field.draw(gc);
            gc.restore();
        }

        gc.restore();
    }

    @Override
    public void reset() {
        for (Pos3x3 p : Pos3x3.values()) {
            getIField(p).reset();
        }
    }
}
