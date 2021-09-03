package composer.MouseEventHandler.ReleaseEventHandler;

import composer.Command.Command;
import composer.Command.CommandWrapper;
import composer.Command.DiscreteCommand.AddNoteCommand;
import composer.Command.DiscreteCommand.SelectCommand;
import composer.Command.DiscreteCommand.UnselectCommand;
import composer.Constants;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.PressEventHandler.PressEventHandler;
import composer.MusicRectangle.MusicRectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


public class PaneReleaseHandler implements ReleaseEventHandler {
    private PaneManager paneManager;
    private CommandWrapper commandWrapper;
    private Boolean wasPlaying;
    private Boolean wasDragSelecting;

    public PaneReleaseHandler(PaneManager paneManager, CommandWrapper commandWrapper, Boolean wasPlaying, boolean wasDragSelecting) {
        this.paneManager = paneManager;
        this.commandWrapper = commandWrapper;
        this.wasPlaying = wasPlaying;
        this.wasDragSelecting = wasDragSelecting;
    }

    public void setWasPlaying(Boolean bool) {
        this.wasPlaying = bool;
    }

    public void setWasDragSelecting(Boolean bool) {
        this.wasDragSelecting = bool;
    }


    public Command handleRelease(MouseEvent mouse, int channel, Paint color){
            //case for drag selecting
            if(wasDragSelecting) {

            }

            else if(mouse.isControlDown()){
                AddNoteCommand addNoteCommand = new AddNoteCommand(paneManager, color, channel, (int) mouse.getX(), (int) mouse.getY(), Constants.RECT_WIDTH);
                addNoteCommand.execute();
                SelectCommand selectCommand = new SelectCommand(this.paneManager, addNoteCommand.getNote());
                selectCommand.execute();
                commandWrapper.add(addNoteCommand);
                commandWrapper.add(selectCommand);
            }
            //clicked on pane
            else{
                Iterator<MusicRectangle> selIt = paneManager.selIterator();
                Collection<MusicRectangle> toUnselect = new HashSet<>();
                while(selIt.hasNext()){
                    MusicRectangle selMusicRect = selIt.next();
                    toUnselect.add(selMusicRect);
                }
                UnselectCommand unSelectAllCommand = new UnselectCommand(paneManager, toUnselect);
                unSelectAllCommand.execute();
                commandWrapper.add(unSelectAllCommand);
                AddNoteCommand addNoteCommand = new AddNoteCommand(paneManager, color, channel, (int) mouse.getX(), (int) mouse.getY(), Constants.RECT_WIDTH);
                addNoteCommand.execute();
                SelectCommand selectCommand = new SelectCommand(this.paneManager, addNoteCommand.getNote());
                selectCommand.execute();
                commandWrapper.add(addNoteCommand);
                commandWrapper.add(selectCommand);
            }
            return commandWrapper;
    }
}
