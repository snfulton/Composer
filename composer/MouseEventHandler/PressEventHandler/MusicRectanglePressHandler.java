package composer.MouseEventHandler.PressEventHandler;

import composer.Command.DiscreteCommand.SelectCommand;
import composer.Command.DiscreteCommand.UnselectCommand;
import composer.Manager.CommandManager;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.ReleaseEventHandler.PaneReleaseHandler;
import composer.MouseEventHandler.ReleaseEventHandler.ReleaseEventHandler;
import composer.MouseEventHandler.ReleaseEventHandler.NoteReleaseHandler;
import composer.MouseEventHandler.DragEventHandler.DragEventHandler;
import composer.MouseEventHandler.DragEventHandler.MovingDragHandler;
import composer.MouseEventHandler.DragEventHandler.StretchingDragHandler;
import composer.MusicRectangle.MusicRectangle;
import javafx.scene.input.MouseEvent;

public class MusicRectanglePressHandler extends PressEventHandler{


    private MusicRectangle pressedMusicRect;

    public MusicRectanglePressHandler(PaneManager paneManager){
        super(paneManager);
    }

    public void setPressedMusicRect(MusicRectangle musicRect){
        this.pressedMusicRect = musicRect;
    }

    public MusicRectangle getPressedMusicRect() { return pressedMusicRect; }

    public void handlePress(MouseEvent mouse) {
            paneManager.moveSelNotesToPrevSelNotes();
            if(mouse.isControlDown()){
                SelectCommand selectPressed = new SelectCommand(paneManager, pressedMusicRect);
                selectPressed.execute();
                commandWrapper.add(selectPressed);
            }
            else{
                UnselectCommand unSelectAllCommand = new UnselectCommand(paneManager, paneManager.getSelNoteSet());
                unSelectAllCommand.execute();
                commandWrapper.add(unSelectAllCommand);
                SelectCommand selectPressed = new SelectCommand(paneManager, pressedMusicRect);
                selectPressed.execute();
                commandWrapper.add(selectPressed);
            }
        mouseX = (int) mouse.getX();
        mouseY = (int) mouse.getY();
        return;
    }
    public DragEventHandler getDragEventHandler(MouseEvent mouse){
        System.out.println("getting drag");
        if(!wasStartedOnSelNote){
            if(!mouse.isControlDown() && !wasStartedOnGesture){
                UnselectCommand unSelectAllCommand = new UnselectCommand(paneManager, paneManager.getSelNoteSet());
                unSelectAllCommand.execute();
                commandWrapper.add(unSelectAllCommand);
            }
            SelectCommand selectPressed = new SelectCommand(paneManager, pressedMusicRect);
            selectPressed.execute();
            commandWrapper.add(selectPressed);
            if (wasStartedOnRightEdge){
                this.dragEventHandler = new StretchingDragHandler(paneManager,commandWrapper, mouseX, mouseY, this.pressedMusicRect);
            }
            else{
                this.dragEventHandler = new MovingDragHandler(paneManager, commandWrapper, this.wasStartedOnGesture, mouseX, mouseY, this.pressedMusicRect);
            }
        }
        else if(!wasStartedOnRightEdge){
            //return new MovingDragHandler
            this.dragEventHandler =  new MovingDragHandler(paneManager,commandWrapper, this.wasStartedOnGesture, mouseX, mouseY, this.pressedMusicRect);
        }
        else {
            //return new StretchingDragHandler
            this.dragEventHandler = new  StretchingDragHandler(paneManager, commandWrapper, mouseX, mouseY, this.pressedMusicRect);
        }
        return this.dragEventHandler;
    }

    @Override
    public ReleaseEventHandler getReleaseEventHandler() {
        if(wasDragged){
            return dragEventHandler.getReleaseEventHandler();
        }
        return new NoteReleaseHandler(paneManager, commandWrapper, false, false, wasStartedOnSelNote, this.pressedMusicRect);
    }
}
