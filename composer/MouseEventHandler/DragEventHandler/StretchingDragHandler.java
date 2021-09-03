package composer.MouseEventHandler.DragEventHandler;

import composer.Command.CommandWrapper;
import composer.Command.ContinuousCommand.ElongateCommand;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.ReleaseEventHandler.ReleaseEventHandler;
import composer.MouseEventHandler.ReleaseEventHandler.NoteReleaseHandler;
import composer.MusicRectangle.MusicRectangle;
import javafx.scene.input.MouseEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class StretchingDragHandler implements DragEventHandler {
    private PaneManager paneManager;
    private CommandWrapper commandWrapper;
    ElongateCommand elongateSelNotes;
    private MusicRectangle pressedMusicRect;

    public StretchingDragHandler(PaneManager paneManager, CommandWrapper commandWrapper, int startX, int startY, MusicRectangle pressedMusicRect){
        Iterator<MusicRectangle> it = paneManager.selIterator();
        Collection<MusicRectangle> toElongate = new HashSet<>();
        this.pressedMusicRect = pressedMusicRect;
        while(it.hasNext()){
            toElongate.add(it.next());
        }
        elongateSelNotes = new ElongateCommand(paneManager, toElongate, startX, startY);
        this.paneManager = paneManager;
        this.commandWrapper = commandWrapper;
    }

    @Override
    public void handleDrag(MouseEvent mouse){
        elongateSelNotes.execute((int) mouse.getX(), (int) mouse.getY());
    }

    @Override
    public ReleaseEventHandler getReleaseEventHandler() {
        commandWrapper.add(elongateSelNotes);
        ReleaseEventHandler releaseHandler = new NoteReleaseHandler(paneManager, this.commandWrapper, false, true, true, this.pressedMusicRect);
        return releaseHandler;
    }
}
