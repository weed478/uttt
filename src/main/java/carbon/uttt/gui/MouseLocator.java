package carbon.uttt.gui;

import carbon.uttt.game.Pos3x3;
import carbon.uttt.game.Pos9x9;
import javafx.beans.property.DoubleProperty;

public class MouseLocator {

    private final DoubleProperty width, height;

    public MouseLocator(DoubleProperty width, DoubleProperty height) {
        this.width = width;
        this.height = height;
    }

    public Pos9x9 locateMouse(double x, double y) {
        if (x < 0 || x >= width.doubleValue() || y < 0 || y >= height.doubleValue())
            return null;

        x *= 3. / width.doubleValue();
        y *= 3. / height.doubleValue();

        int dpx = (int) x;
        int dpy = (int) y;

        int mpx = (int) (((x - dpx - 0.05) / 0.9 - 0.05) / 0.9 * 3.);
        int mpy = (int) (((y - dpy - 0.05) / 0.9 - 0.05) / 0.9 * 3.);

        if (mpx < 0 || mpx > 2 || mpy < 0 || mpy > 2)
            return null;

        return new Pos9x9(Pos3x3.fromXY(dpx, dpy), Pos3x3.fromXY(mpx, mpy));
    }
}
