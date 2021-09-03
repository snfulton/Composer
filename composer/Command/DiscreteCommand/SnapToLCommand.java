package composer.Command.DiscreteCommand;

import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

public class SnapToLCommand implements DiscreteCommand, Visitor, UndoVisitor {
    private MusicRectangle musicRectangle;
    private int changeInY;

    public SnapToLCommand( MusicRectangle musicRectangle){
        this.musicRectangle = musicRectangle;
    }


    @Override
    public void undo() {
        musicRectangle.acceptUndo(this);
    }

    @Override
    public void redo() {
        this.execute();
    }

    @Override
    public void execute() {
        int rectY = (int) Math.round((float) musicRectangle.getY()/ 10)*10;
        changeInY = (int) musicRectangle.getY() - rectY;
        musicRectangle.accept(this);
    }

    @Override
    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        System.out.println(changeInY);
        currentMusicRectangle.setY(currentMusicRectangle.getY() - changeInY);
    }

    @Override
    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        System.out.println(changeInY);
        currentMusicRectangle.setY(currentMusicRectangle.getY() - changeInY);
    }

    @Override
    public void visitGesture(GestureRectangle musicRectangle) {
        System.out.println(changeInY);
        musicRectangle.setY(musicRectangle.getY()-changeInY);
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        System.out.println(changeInY);
        musicRectangle.setY(musicRectangle.getY()-changeInY);
    }

    @Override
    public void visitGestureUndo(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        currentMusicRectangle.setY(currentMusicRectangle.getY() + changeInY);
    }

    @Override
    public void visitNoteUndo(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        currentMusicRectangle.setY(currentMusicRectangle.getY() + changeInY);
    }

    @Override
    public void visitGestureUndo(GestureRectangle musicRectangle) {
        musicRectangle.setY(musicRectangle.getY() + changeInY);
    }

    @Override
    public void visitNoteUndo(NoteRectangle musicRectangle) {
        musicRectangle.setY(musicRectangle.getY() + changeInY);
    }
}
