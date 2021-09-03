package composer.MusicRectangle;

import composer.BooleanChecker.BooleanChecker;
import composer.Command.UndoVisitor;
import composer.Command.Visitor;

import java.util.Iterator;

public class NoteRectangle extends MusicRectangle{
/*
 * This is a private nested class which inherits from the Rectangle class supplied by JavaFX
 * It has a few extra variables: pitch, startTick, and endTick, as well as public methods to access them
 */

    public NoteRectangle(int x, int y, int width, int height, int channel){
        super(x, y, width, height, channel);
        this.youngestAncestor = this;
    }

    public void setDefaultStyle(){
        this.getStyleClass().clear();
        this.getStyleClass().add("note-rectangle-outline");
    }

    public void setSelectedStyle(){
        this.getStyleClass().clear();
        this.getStyleClass().add("selected-note-rectangle-outline");
    }

    @Override
    public boolean isTrueForAll(BooleanChecker booleanChecker) {
        return booleanChecker.check(this);
    }

    @Override
    public boolean isTrueForOne(BooleanChecker booleanChecker) {
        return booleanChecker.check(this);
    }

    @Override
    public void accept(Visitor command) {
        command.visitNote(this);
    }

    @Override
    public void accept(Visitor command, GestureRectangle parentMusicRectangle) {
        command.visitNote(parentMusicRectangle, this);
    }

    @Override
    public void acceptUndo(UndoVisitor command) {
        command.visitNoteUndo(this);
    }

    @Override
    public void acceptUndo(UndoVisitor command, GestureRectangle parentMusicRectangle) {
        command.visitNoteUndo(parentMusicRectangle, this);
    }

}



