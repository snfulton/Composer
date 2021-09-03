package composer.MouseEventHandler.DragEventHandler;
import composer.Command.CommandWrapper;
import composer.Command.ContinuousCommand.MoveCommand;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.ReleaseEventHandler.ReleaseEventHandler;
import composer.MouseEventHandler.ReleaseEventHandler.NoteReleaseHandler;
import composer.MusicRectangle.MusicRectangle;
import javafx.scene.input.MouseEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class MovingDragHandler implements DragEventHandler {
    private PaneManager paneManager;
    private CommandWrapper commandWrapper;
    private boolean isStartedOnGesture;
    private int startY;
    private int startX;
    private MoveCommand moveSelNotes;
    private MusicRectangle pressedMusicRect;

    public MovingDragHandler(PaneManager paneManager, CommandWrapper commandWrapper, boolean isStartedOnGesture, int startX, int startY, MusicRectangle pressedMusicRect){
        this.paneManager = paneManager;
        this.commandWrapper = commandWrapper;
        this.isStartedOnGesture = isStartedOnGesture;
        this.startX = startX;
        this.startY = startY;
        this.pressedMusicRect = pressedMusicRect;
        Iterator<MusicRectangle> it = paneManager.selIterator();
        Collection<MusicRectangle> toMove = new HashSet<>();
        while(it.hasNext()){
            toMove.add(it.next());
        }
        moveSelNotes = new MoveCommand(paneManager, toMove, startX, startY);
    }

    @Override
    public void handleDrag(MouseEvent mouse) {
        moveSelNotes.execute((int)mouse.getX(), (int)mouse.getY());
    }

    @Override
    public ReleaseEventHandler getReleaseEventHandler() {
        commandWrapper.add(moveSelNotes);
        return new NoteReleaseHandler(paneManager, this.commandWrapper, true, false, true, this.pressedMusicRect);
    }
}
