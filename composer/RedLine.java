package composer;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.layout.Pane;

public class RedLine {
    private final Line redLine;
    private TranslateTransition redLineTranslator;

    public RedLine() {
        redLine= new Line(0, 0, 0, Constants.NOTE_INPUT_PANE_WIDTH);
    }

    public void setupRedLine() {
        redLine.getStyleClass().add("redline");
        redLineTranslator = new TranslateTransition(Duration.millis(0), redLine);
    }

    public void stopRedLine(Pane noteInputPane){
        if (noteInputPane.getChildren().contains(redLine)){
            noteInputPane.getChildren().removeAll(redLine);
        }
        redLineTranslator.stop();
        redLine.setTranslateX(0);
    }

    public void animateTimeline(Pane noteInputPane, int paneEndTick) {
        redLineTranslator = new TranslateTransition(Duration.millis(1000 * paneEndTick / Constants.TICKS_PER_SECOND), redLine);
        redLineTranslator.setInterpolator(Interpolator.LINEAR);
        redLine.setTranslateX(0);
        redLineTranslator.setFromX(0);
        redLineTranslator.setToX(paneEndTick);
        redLineTranslator.setOnFinished(finish -> {
            stopRedLine(noteInputPane);
        });
        noteInputPane.getChildren().add(redLine);
        redLineTranslator.play();
    }
}
