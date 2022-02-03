package carbon.uttt.game;

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
}
