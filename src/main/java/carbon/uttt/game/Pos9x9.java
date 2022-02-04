package carbon.uttt.game;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Full position identifying a single
 * field on a global board.
 * Combination of local board position
 * within global board and field position
 * within local board.
 */
public class Pos9x9 {

    private final Pos3x3 gp, lp;

    /**
     * Create a full position.
     * @param gp Local board withing global board.
     * @param lp Field within local board.
     */
    public Pos9x9(Pos3x3 gp, Pos3x3 lp) {
        this.gp = gp;
        this.lp = lp;
    }

    /**
     * Get local board position within global board.
     * @return 3x3 position of local board.
     */
    public Pos3x3 gp() {
        return gp;
    }

    /**
     * Get field position within local board.
     * @return 3x3 position of field.
     */
    public Pos3x3 lp() {
        return lp;
    }

    /**
     * Get all possible full positions.
     * @return Stream of positions.
     */
    public static Stream<Pos9x9> values() {
        return Arrays.stream(Pos3x3.values())
                .flatMap(gp -> Arrays.stream(Pos3x3.values())
                        .map(lp -> new Pos9x9(gp, lp)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos9x9 posDPMP = (Pos9x9) o;
        return gp == posDPMP.gp && lp == posDPMP.lp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gp, lp);
    }

    @Override
    public String toString() {
        return gp + "/" + lp;
    }
}
