package carbon.uttt.game;

public enum Pos3x3 {
    NW, N, NE,
     W, C,  E,
    SW, S, SE;

    public static Pos3x3 fromXY(int x, int y) {
        if (x == 0) {
            if (y == 0) {
                return NW;
            }
            else if (y == 1) {
                return N;
            }
            else if (y == 2) {
                return NE;
            }
        }
        else if (x == 1) {
            if (y == 0) {
                return W;
            }
            else if (y == 1) {
                return C;
            }
            else if (y == 2) {
                return E;
            }
        }
        else if (x == 2) {
            if (y == 0) {
                return SW;
            }
            else if (y == 1) {
                return S;
            }
            else if (y == 2) {
                return SE;
            }
        }

        throw new IllegalArgumentException("Invalid x or y");
    }

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
