package composer.MouseEventHandler.DragEventHandler;

import composer.Command.CommandWrapper;
import composer.Command.DiscreteCommand.SelectCommand;
import composer.Command.DiscreteCommand.UnselectCommand;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.ReleaseEventHandler.ReleaseEventHandler;
import composer.MouseEventHandler.ReleaseEventHandler.PaneReleaseHandler;
import composer.MusicRectangle.MusicRectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


public class DragSelectDragHandler implements DragEventHandler {
    private Pane pane;
    private Point lastPoint;
    private PaneManager paneManager;
    private CommandWrapper commandWrapper;
    boolean wasPlaying;
    Collection<MusicRectangle> toSelect;

    public DragSelectDragHandler(PaneManager paneManager, CommandWrapper commandWrapper, int x, int y, Boolean wasPlaying){
        this.paneManager = paneManager;
        this.commandWrapper = commandWrapper;
        paneManager.setUpDragSelectRect(x, y);
        lastPoint = new Point(x, y);
        this.wasPlaying = wasPlaying;
        this.toSelect = new HashSet<>();
    }

    public void handleDrag(MouseEvent mouse) {
        if (mouse.getX() < lastPoint.getX())
            paneManager.changeDragSelectX(mouse.getX());
        if (mouse.getY() < lastPoint.getY())
            paneManager.changeDragSelectY(mouse.getY());
        int dragRectangleWidth = (int) Math.abs(mouse.getX() - lastPoint.getX());
        int dragRectangleHeight = (int) Math.abs(mouse.getY() - lastPoint.getY());
        paneManager.changeDragSelectSize(dragRectangleWidth, dragRectangleHeight);
        Iterator<MusicRectangle> it = paneManager.allIterator();
        Rectangle dragSelectRectangle = paneManager.getDragSelectRectangle();
        while(it.hasNext()){
            MusicRectangle musicRectangle = it.next();
            if(dragSelectRectangle.intersects(musicRectangle.getBoundsInLocal())){
                MusicRectangle musicRectSelect = musicRectangle.getYoungestAncestor();
                SelectCommand selectCommand = new SelectCommand(paneManager, musicRectSelect);
                selectCommand.execute();
                toSelect.add(musicRectSelect);
            }else {
                MusicRectangle musicRectUnselect = musicRectangle.getYoungestAncestor();
                UnselectCommand unselectCommand = new UnselectCommand(paneManager, musicRectUnselect);
                unselectCommand.execute();
                toSelect.remove(musicRectUnselect);
            }
        }
    }

    @Override
    public ReleaseEventHandler getReleaseEventHandler() {
        paneManager.removeDragSelectRect();
        Iterator<MusicRectangle> selIt = paneManager.selIterator();
        Collection<MusicRectangle> toUnselect = new HashSet<>();
        while(selIt.hasNext()){
            toUnselect.add(selIt.next());
        }
        UnselectCommand unselectCommand = new UnselectCommand(paneManager, toUnselect);
        unselectCommand.execute();
        commandWrapper.add(unselectCommand);

        SelectCommand selectCommand = new SelectCommand(paneManager, toSelect);
        selectCommand.execute();
        commandWrapper.add(selectCommand);



        PaneReleaseHandler clickEventHandler = new PaneReleaseHandler(paneManager, commandWrapper, this.wasPlaying, true);
        return clickEventHandler;
    }
}
