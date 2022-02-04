package carbon.uttt.game;

/**
 * Position on a 3x3 board.
 */
public enum Pos3x3 {
    NW, N, NE,
     W, C,  E,
    SW, S, SE;

    /**
     * Create a position from x/y coordinates.
     * 0/0 is top left, 2/0 is top right.
     * @param x [0, 2]
     * @param y [0, 2]
     * @return 3x3 position
     */
    public static Pos3x3 fromXY(int x, int y) {
        if (y == 0) {
            if (x == 0) {
                return NW;
            }
            else if (x == 1) {
                return N;
            }
            else if (x == 2) {
                return NE;
            }
        }
        else if (y == 1) {
            if (x == 0) {
                return W;
            }
            else if (x == 1) {
                return C;
            }
            else if (x == 2) {
                return E;
            }
        }
        else if (y == 2) {
            if (x == 0) {
                return SW;
            }
            else if (x == 1) {
                return S;
            }
            else if (x == 2) {
                return SE;
            }
        }

        throw new IllegalArgumentException("Invalid x or y");
    }

    /**
     * Get X coordinate of this position.
     * @return X coordinate [0, 2]
     */
    public int getX() {
        switch (this) {
            case NW:
            case W:
            case SW:
                return 0;
            case N:
            case C:
            case S:
                return 1;
            default:
                return 2;
        }
    }

    /**
     * Get Y coordinate of this position.
     * @return Y coordinate [0, 2]
     */
    public int getY() {
        switch(this) {
            case NW:
            case N:
            case NE:
                return 0;
            case W:
            case C:
            case E:
                return 1;
            default:
                return 2;
        }
    }
}
