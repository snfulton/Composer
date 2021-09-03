package composer.Command.DiscreteCommand;

import composer.Command.Command;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectSetComparitor;
import composer.MusicRectangle.MusicRectangle;

import java.util.Collection;

public class GroupCommand implements Command {

    private GestureRectangle gesture;
    private SelectCommand selCommand;
    private PaneManager paneManager;
    private Collection<MusicRectangle> toGroup;

    public GroupCommand(PaneManager paneManager, Collection<MusicRectangle> toGroup){
        this.paneManager = paneManager;
        this.toGroup = toGroup;
    }

    public void execute(){
        int xCord = MusicRectSetComparitor.getMinIntegerProperty(toGroup, musicrect -> (int)musicrect.getX());
        int yCord = MusicRectSetComparitor.getMinIntegerProperty(toGroup, musicrect -> (int)musicrect.getY());
        int bottomY = MusicRectSetComparitor.getMaxIntegerProperty(toGroup, musicrect -> musicrect.getBottomY());
        int rightX = MusicRectSetComparitor.getMaxIntegerProperty(toGroup, musicrect -> musicrect.getRightX());
        int height = bottomY - yCord;
        int width = rightX - xCord;
        this.gesture = new GestureRectangle(xCord, yCord, width, height, toGroup);
        paneManager.addToAll(gesture);
        paneManager.addToPane(gesture);
        for(MusicRectangle musicRectangle : this.toGroup){
            paneManager.removeFromAll(musicRectangle);
            paneManager.removeFromSel(musicRectangle);
        }
//        selCommand = new SelectCommand(paneManager, gesture);
//        selCommand.execute();
    }
    public void undo(){
        for(MusicRectangle musicRectangle :toGroup ){
            SetYACommand setYACommand = new SetYACommand(musicRectangle);
            setYACommand.execute();
            paneManager.addToAll(musicRectangle);
        }
        paneManager.removeFromAll(gesture);
        paneManager.removeFromPane(gesture);
    }

    public void redo(){
        SetYACommand setYACommand = new SetYACommand(gesture);
        setYACommand.execute();
        for(MusicRectangle musicRectangle :toGroup ){
            paneManager.removeFromAll(musicRectangle);
        }
        paneManager.addToAll(gesture);
        paneManager.addToPane(gesture);
    }

    public GestureRectangle getGesture(){
        return this.gesture;
    }
}
