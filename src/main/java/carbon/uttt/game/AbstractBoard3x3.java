package carbon.uttt.game;

import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractBoard3x3 implements IBoard3x3 {

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.scale(1./3, 1./3);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Pos3x3 pos = Pos3x3.fromXY(x, y);

                gc.save();
                gc.translate(x, y);
                IField field = getFieldAt(pos);
                field.draw(gc);
                gc.restore();
            }
        }

        gc.restore();
    }
}
