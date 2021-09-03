package composer.Command.DiscreteCommand;

import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

public class SetYACommand implements DiscreteCommand, Visitor, UndoVisitor {
    private MusicRectangle formerYoungest;
    private MusicRectangle musicRectangle;

    public SetYACommand(MusicRectangle musicRectangle){
        this.musicRectangle = musicRectangle;
    }

    @Override
    public void undo() {
        this.musicRectangle.acceptUndo(this);
    }

    @Override
    public void redo() {
        this.execute();
    }

    @Override
    public void execute() {
        this.musicRectangle.accept(this);
    }

    @Override
    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        currentMusicRectangle.setYoungestAncestor(parentMusicRectangle.getYoungestAncestor());
    }

    @Override
    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        currentMusicRectangle.setYoungestAncestor(parentMusicRectangle.getYoungestAncestor());
    }

    @Override
    public void visitGesture(GestureRectangle musicRectangle) {
        formerYoungest = musicRectangle.getYoungestAncestor();
        musicRectangle.setYoungestAncestor(musicRectangle);
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        formerYoungest = musicRectangle.getYoungestAncestor();
        musicRectangle.setYoungestAncestor(musicRectangle);
    }

    @Override
    public void visitGestureUndo(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        currentMusicRectangle.setYoungestAncestor(parentMusicRectangle.getYoungestAncestor());
    }

    @Override
    public void visitNoteUndo(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        currentMusicRectangle.setYoungestAncestor(parentMusicRectangle.getYoungestAncestor());
    }

    @Override
    public void visitGestureUndo(GestureRectangle musicRectangle) {
        musicRectangle.setYoungestAncestor(formerYoungest);
    }

    @Override
    public void visitNoteUndo(NoteRectangle musicRectangle) {
        musicRectangle.setYoungestAncestor(formerYoungest);
    }
}
