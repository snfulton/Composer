package composer.Command.ContinuousCommand;

import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

import java.awt.*;
import java.util.Collection;

public class MoveCommand implements ContinuousCommand, Visitor, UndoVisitor {

    private Collection<MusicRectangle> movedNotes;
    private PaneManager paneManager;
    private Point lastPoint;
    private Point startingPoint;
    private int changeInX;
    private int changeInY;

    public MoveCommand(PaneManager paneManager, Collection<MusicRectangle> movedNotes, int startingX, int startingY){
        this.paneManager = paneManager;
        this.startingPoint = new Point(startingX, startingY);
        this.lastPoint = new Point(startingX, startingY);
        this.movedNotes = movedNotes;
    }

    public void execute(int currX, int currY){
        changeInX = currX - (int) lastPoint.getX();
        changeInY = currY - (int) lastPoint.getY();
        for(MusicRectangle musicRectangle : movedNotes){
            musicRectangle.accept(this);
        }
        lastPoint.setLocation(currX, currY);
    }
    
    public void undo(){
        changeInX = (int) (startingPoint.getX()-lastPoint.getX());
        changeInY = (int) (startingPoint.getY()-lastPoint.getY());
        for(MusicRectangle musicRectangle : movedNotes){
            musicRectangle.acceptUndo(this);
        }
    }
    public void redo(){
        changeInX = (int) (lastPoint.getX()-startingPoint.getX());
        changeInY = (int) (lastPoint.getY()-startingPoint.getY());
        for(MusicRectangle musicRectangle: movedNotes){
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
        int newX = (int) (changeInX + musicRectangle.getX());
        int newY = (int) (changeInY + musicRectangle.getY());
        musicRectangle.setX(newX);
        musicRectangle.setY(newY);
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        int newX = (int) (changeInX + musicRectangle.getX());
        int newY = (int) (changeInY + musicRectangle.getY());
        musicRectangle.setX(newX);
        musicRectangle.setY(newY);
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
