package composer.Command;

import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

public interface Visitor {
    void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle);
    void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle);
    void visitGesture(GestureRectangle musicRectangle);
    void visitNote(NoteRectangle musicRectangle);
}
