package composer.MouseEventHandler.DragEventHandler;
import composer.MouseEventHandler.ReleaseEventHandler.ReleaseEventHandler;
import javafx.scene.input.MouseEvent;


public interface DragEventHandler {
    abstract public void handleDrag(MouseEvent mouse);
    abstract public ReleaseEventHandler getReleaseEventHandler();
}
