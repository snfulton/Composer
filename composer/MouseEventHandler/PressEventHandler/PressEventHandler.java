package composer.MouseEventHandler.PressEventHandler;

import composer.Command.CommandWrapper;
import composer.Manager.CommandManager;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.ReleaseEventHandler.ReleaseEventHandler;
import composer.MouseEventHandler.DragEventHandler.DragEventHandler;
import javafx.scene.input.MouseEvent;

public abstract class PressEventHandler {
    protected Boolean wasStartedOnNote;
    protected Boolean wasStartedOnSelNote;
    protected Boolean wasStartedOnRightEdge;
    protected Boolean wasDragged;
    protected Boolean wasStartedOnGesture;
    protected Boolean wasPlaying;
    protected int mouseX;
    protected int mouseY;
    protected CommandWrapper commandWrapper;
    protected DragEventHandler dragEventHandler;
    protected PaneManager paneManager;

    public PressEventHandler(PaneManager paneManager){
        this.wasStartedOnNote = false;
        this.wasStartedOnSelNote = false;
        this.wasStartedOnRightEdge = false;
        this.wasPlaying = false;
        this.wasDragged = false;
        this.wasStartedOnGesture = false;
        this.mouseX = -1;
        this.mouseX = -1;
        commandWrapper = new CommandWrapper();
        this.paneManager = paneManager;
    }

    public void setWasStartedOnNote(Boolean bool){
        this.wasStartedOnNote = bool;
    }

    public void setWasStartedOnSelNote(Boolean bool){
        this.wasStartedOnSelNote = bool;
    }

    public void setWasStartedOnRightEdge(Boolean bool){
        this.wasStartedOnRightEdge = bool;
    }

    public void setWasPlaying(Boolean bool){ this.wasPlaying = bool; }

    public void setWasDragged(Boolean bool){ this.wasDragged = bool;}

    public void setWasStartedOnGesture(Boolean bool){ this.wasStartedOnGesture = bool;
    }

    abstract public void handlePress(MouseEvent mouse);

    abstract public DragEventHandler getDragEventHandler(MouseEvent mouse);

    public abstract ReleaseEventHandler getReleaseEventHandler();

    public Boolean wasDragged(){
        return wasDragged;
    }
}
