package composer.Command;

import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.NoteRectangle;

public interface UndoVisitor {
    abstract public void visitGestureUndo(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle);
    abstract public void visitNoteUndo(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle);
    abstract public void visitGestureUndo(GestureRectangle musicRectangle);
    abstract public void visitNoteUndo(NoteRectangle musicRectangle);
}
