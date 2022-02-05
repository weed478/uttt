package carbon.uttt.ai;

import carbon.uttt.game.IGame;
import carbon.uttt.game.Pos9x9;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomAI implements IAI {

    private final IGame game;

    public RandomAI(IGame game) {
        this.game = game;
    }

    @Override
    public Pos9x9 decideMove() {
        List<Pos9x9> moves = Pos9x9
                .values()
                .filter(game::moveValid)
                .collect(Collectors.toList());

        if (moves.isEmpty()) {
            throw new IllegalArgumentException("Cannot make a move");
        }

        int i = new Random().nextInt(moves.size());
        return moves.get(i);
    }
}
