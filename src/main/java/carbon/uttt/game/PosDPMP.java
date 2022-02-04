package carbon.uttt.game;

import java.util.Objects;

public class PosDPMP {

    private final Pos3x3 dp, mp;

    public PosDPMP(Pos3x3 dp, Pos3x3 mp) {
        this.dp = dp;
        this.mp = mp;
    }

    public Pos3x3 dp() {
        return dp;
    }

    public Pos3x3 mp() {
        return mp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosDPMP posDPMP = (PosDPMP) o;
        return dp == posDPMP.dp && mp == posDPMP.mp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dp, mp);
    }

    @Override
    public String toString() {
        return dp + "/" + mp;
    }
}
