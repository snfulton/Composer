package composer.Command.ContinuousCommand;

import composer.BooleanChecker.BooleanChecker;
import composer.BooleanChecker.WouldBeTooSmall;
import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

import java.awt.*;
import java.util.Collection;

public class ElongateCommand implements ContinuousCommand, Visitor, UndoVisitor {

    private Collection<MusicRectangle> elongatednotes;
    private Point startingPoint;
    private PaneManager paneManager;
    private Point lastPoint;
    private int changeInX;

    public ElongateCommand(PaneManager paneManager, Collection<MusicRectangle> elongatednotes, int startingX, int startingY){
        this.paneManager = paneManager;
        lastPoint = new Point(startingX, startingY);
        startingPoint = new Point(startingX, startingY);
        this.elongatednotes = elongatednotes;
    }

    public void execute(int currX, int currY){
        changeInX = currX - (int) lastPoint.getX();
        for(MusicRectangle musicRectangle: elongatednotes){
            BooleanChecker booleanChecker = new WouldBeTooSmall(changeInX);
            boolean wouldBeTooSmall = musicRectangle.isTrueForOne(booleanChecker);
            if(! wouldBeTooSmall){
                musicRectangle.accept(this);
                lastPoint.setLocation(currX, currY);
            }
        }
    }
    
    public void undo(){
        changeInX = (int) (startingPoint.getX()-lastPoint.getX());
        for(MusicRectangle musicRectangle: elongatednotes){
            musicRectangle.acceptUndo(this);
        }
    }
    public void redo(){
        changeInX = (int) (lastPoint.getX()-startingPoint.getX());
        for(MusicRectangle musicRectangle: elongatednotes){
            musicRectangle.accept(this);
        }
    }

    @Override
    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        this.visitGesture(currentMusicRectangle);
    }

    @Override
    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        this.visitNote(currentMusicRectangle);
    }

    @Override
    public void visitGesture(GestureRectangle musicRectangle) {
        musicRectangle.setWidth(musicRectangle.getWidth() + changeInX);
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        musicRectangle.setWidth(musicRectangle.getWidth() + changeInX);
    }

    @Override
    public void visitGestureUndo(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        this.visitGesture(currentMusicRectangle);
    }

    @Override
    public void visitNoteUndo(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        this.visitNote(currentMusicRectangle);
    }

    @Override
    public void visitGestureUndo(GestureRectangle musicRectangle) {
        this.visitGesture(musicRectangle);
    }

    @Override
    public void visitNoteUndo(NoteRectangle musicRectangle) {
        this.visitNote(musicRectangle);
    }
}
