package composer.MusicRectangle;

import composer.BooleanChecker.BooleanChecker;
import composer.Command.DiscreteCommand.SetYACommand;
import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class GestureRectangle extends MusicRectangle {

    private HashSet<MusicRectangle> gestureNotes;

    public GestureRectangle(int x, int y, int width, int height, Collection<MusicRectangle> musicRects){
        super(x,y,width,height,15);
        gestureNotes = new HashSet<>();
        for(MusicRectangle musicRectangle : musicRects){
            this.gestureNotes.add(musicRectangle);
        }
        this.setFill(Color.TRANSPARENT);
        this.setViewOrder(1);
        this.getStyleClass().add("gesture-rectangle-outline");
        SetYACommand setYACommand = new SetYACommand(this);
        setYACommand.execute();
    }

    public void addToGesture(MusicRectangle musicRectangle){
        gestureNotes.add(musicRectangle);
    }

    @Override
    public boolean isTrueForAll(BooleanChecker booleanChecker) {
        boolean toReturn = booleanChecker.check(this);
        for(MusicRectangle musicRectangle : gestureNotes){
            toReturn = toReturn && musicRectangle.isTrueForAll(booleanChecker);
        }
        return toReturn;
    }

    @Override
    public boolean isTrueForOne(BooleanChecker booleanChecker) {
        boolean toReturn = booleanChecker.check(this);
        for(MusicRectangle musicRectangle : gestureNotes){
            toReturn = toReturn || musicRectangle.isTrueForAll(booleanChecker);
        }
        return toReturn;
    }

    @Override
    public void accept(Visitor command) {
        command.visitGesture(this);
        for (MusicRectangle child : gestureNotes){
            child.accept(command, this);
        }
    }

    @Override
    public void accept(Visitor command, GestureRectangle parentMusicRectangle) {
        command.visitGesture(parentMusicRectangle, this);
        for (MusicRectangle child : gestureNotes){
            child.accept(command, this);
        }
    }

    @Override
    public void acceptUndo(UndoVisitor command) {
        command.visitGestureUndo(this);
        for (MusicRectangle child : gestureNotes){
            child.acceptUndo(command, this);
        }
    }

    @Override
    public void acceptUndo(UndoVisitor command, GestureRectangle parentMusicRectangle) {
        command.visitGestureUndo(parentMusicRectangle, this);
        for (MusicRectangle child : gestureNotes){
            child.acceptUndo(command, this);
        }
    }


}