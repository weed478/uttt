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

    @Override
    public Player getPlayer() {
        // vertical
        for (int x = 0; x < 3; x++) {
            Player player = getIField(Pos3x3.fromXY(x, 0)).getPlayer();
            for (int y = 1; y < 3; y++) {
                Pos3x3 pos = Pos3x3.fromXY(x, y);
                if (!getIField(pos).getPlayer().equals(player)) {
                    player = Player.EMPTY;
                    break;
                }
            }
            if (!player.isEmpty()) return player;
        }

        // horizontal
        for (int y = 0; y < 3; y++) {
            Player player = getIField(Pos3x3.fromXY(0, y)).getPlayer();
            for (int x = 1; x < 3; x++) {
                Pos3x3 pos = Pos3x3.fromXY(x, y);
                if (!getIField(pos).getPlayer().equals(player)) {
                    player = Player.EMPTY;
                    break;
                }
            }
            if (!player.isEmpty()) return player;
        }

        // diagonal \
        {
            Player player = getIField(Pos3x3.fromXY(0, 0)).getPlayer();
            for (int xy = 1; xy < 3; xy++) {
                Pos3x3 pos = Pos3x3.fromXY(xy, xy);
                if (!getIField(pos).getPlayer().equals(player)) {
                    player = Player.EMPTY;
                    break;
                }
            }
            if (!player.isEmpty()) return player;
        }

        // diagonal /
        {
            Player player = getIField(Pos3x3.fromXY(0, 2)).getPlayer();
            for (int xy = 1; xy < 3; xy++) {
                Pos3x3 pos = Pos3x3.fromXY(xy, 2 - xy);
                if (!getIField(pos).getPlayer().equals(player)) {
                    player = Player.EMPTY;
                    break;
                }
            }
            if (!player.isEmpty()) return player;
        }

        return Player.EMPTY;
    }
}
