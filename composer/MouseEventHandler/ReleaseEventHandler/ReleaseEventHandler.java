package composer.MouseEventHandler.ReleaseEventHandler;


import composer.Command.Command;
import composer.MouseEventHandler.PressEventHandler.PressEventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

public interface ReleaseEventHandler {

    public Command handleRelease(MouseEvent mouse, int channel, Paint color);
}
