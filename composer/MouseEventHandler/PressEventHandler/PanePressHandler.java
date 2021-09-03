package composer.MouseEventHandler.PressEventHandler;

import composer.Command.DiscreteCommand.UnselectCommand;
import composer.Manager.CommandManager;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.ReleaseEventHandler.ReleaseEventHandler;
import composer.MouseEventHandler.ReleaseEventHandler.PaneReleaseHandler;
import composer.MouseEventHandler.DragEventHandler.DragEventHandler;
import composer.MouseEventHandler.DragEventHandler.DragSelectDragHandler;
import javafx.scene.input.MouseEvent;

public class PanePressHandler extends PressEventHandler{


    public PanePressHandler(PaneManager paneManager){
        super(paneManager);
    }

    public void handlePress(MouseEvent mouse) {
        paneManager.moveSelNotesToPrevSelNotes();
        if(wasStartedOnGesture && !mouse.isControlDown()){
            UnselectCommand unSelectAllCommand = new UnselectCommand(paneManager, paneManager.getSelNoteSet());
            unSelectAllCommand.execute();
            commandWrapper.add(unSelectAllCommand);
        }
        mouseX = (int) mouse.getX();
        mouseY = (int) mouse.getY();
        return;
    }

    public DragEventHandler getDragEventHandler(MouseEvent mouse){
        System.out.println("pane press handle drag");
        this.dragEventHandler = new DragSelectDragHandler(paneManager, commandWrapper, (int) mouse.getX(), (int) mouse.getY(), this.wasPlaying);
        return this.dragEventHandler;
    }

    @Override
    public ReleaseEventHandler getReleaseEventHandler() {
        if(!wasDragged){
            return new PaneReleaseHandler(paneManager, commandWrapper, this.wasPlaying, false);
        }
        return dragEventHandler.getReleaseEventHandler();
    }


}
