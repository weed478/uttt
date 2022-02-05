package carbon.uttt.gui.game;

public class GameConfiguration {

    public final IInteractiveGame game;

    public final Integer timeLimitSeconds;

    public GameConfiguration(IInteractiveGame game, String timeLimit) {
        this.game = game;
        if (timeLimit == null) {
            this.timeLimitSeconds = null;
        }
        else {
            this.timeLimitSeconds = Integer.parseInt(timeLimit);
        }
    }
}
