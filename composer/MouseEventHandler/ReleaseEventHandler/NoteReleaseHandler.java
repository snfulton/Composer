package composer.MouseEventHandler.ReleaseEventHandler;

import composer.Command.Command;
import composer.Command.CommandWrapper;
import composer.Command.DiscreteCommand.UnselectCommand;
import composer.Command.DiscreteCommand.SnapToLCommand;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.PressEventHandler.MusicRectanglePressHandler;
import composer.MouseEventHandler.PressEventHandler.PressEventHandler;
import composer.MusicRectangle.MusicRectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.Iterator;

public class NoteReleaseHandler implements ReleaseEventHandler {
    private Boolean isMovingSelNote;
    private Boolean isStretchingSelNote;
    private Boolean isStartedOnSelNote;
    private Boolean isStartedOnGesture;
    private PaneManager paneManager;
    private CommandWrapper commandWrapper;
    private MusicRectangle musicRectanglePressed;

    public NoteReleaseHandler(PaneManager paneManager, CommandWrapper commandWrapper, boolean isMovingSelNote, boolean isStretchingSelNote, boolean isStartedOnSelNote, MusicRectangle musicRectanglePressed){
        this.paneManager = paneManager;
        this.isMovingSelNote = isMovingSelNote;
        this.isStretchingSelNote = isStretchingSelNote;
        this.isStartedOnSelNote = isStartedOnSelNote;
        this.isStartedOnGesture = isStartedOnGesture;
        this.commandWrapper = commandWrapper;
        this.musicRectanglePressed = musicRectanglePressed;
    }

    public Command handleRelease(MouseEvent mouse, int channel, Paint color) {
        System.out.println("made it to click");

        Iterator<MusicRectangle> it = paneManager.allIterator();
        while(it.hasNext()){
            SnapToLCommand updateNoteData = new SnapToLCommand(it.next());
            updateNoteData.execute();
            commandWrapper.add(updateNoteData);
        }

        if(!isMovingSelNote &&!isStretchingSelNote && isStartedOnSelNote && mouse.isControlDown()){
            UnselectCommand unselectCommand = new UnselectCommand(paneManager, this.musicRectanglePressed);
            unselectCommand.execute();
            commandWrapper.add(unselectCommand);
        }
        return commandWrapper;
    }
}
