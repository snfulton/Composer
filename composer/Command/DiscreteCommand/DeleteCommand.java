package composer.Command.DiscreteCommand;

import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

import java.util.Collection;
import java.util.HashSet;

public class DeleteCommand implements DiscreteCommand, Visitor, UndoVisitor {

    private Collection<MusicRectangle> deletednotes;
    private PaneManager paneManager;

    public DeleteCommand(PaneManager paneManager, MusicRectangle musicRectangle){
        this.paneManager = paneManager;
        this.deletednotes = new HashSet<>();
        this.deletednotes.add(musicRectangle);
    }

    public DeleteCommand(PaneManager paneManager, Collection<MusicRectangle> musicRectangle){
        this.paneManager = paneManager;
        this.deletednotes = musicRectangle;
    }

    public void execute(){
        for(MusicRectangle musicRectangle : deletednotes){
            musicRectangle.accept(this);
        }
    }

    public void undo(){
        for(MusicRectangle musicRectangle : deletednotes){
            musicRectangle.acceptUndo(this);
        }
    }
    public void redo(){
        this.execute();
    }

    @Override
    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        paneManager.removeFromPane(currentMusicRectangle);
    }

    @Override
    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        paneManager.removeFromPane(currentMusicRectangle);
    }

    @Override
    public void visitGesture(GestureRectangle musicRectangle) {
        paneManager.removeFromPane(musicRectangle);
        paneManager.removeFromAll(musicRectangle);
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        paneManager.removeFromPane(musicRectangle);
        paneManager.removeFromAll(musicRectangle);
    }

    @Override
    public void visitGestureUndo(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        paneManager.addToPane(currentMusicRectangle);
    }

    @Override
    public void visitNoteUndo(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        paneManager.addToPane(currentMusicRectangle);
    }

    @Override
    public void visitGestureUndo(GestureRectangle musicRectangle) {
        paneManager.addToPane(musicRectangle);
        paneManager.addToAll(musicRectangle);
    }

    @Override
    public void visitNoteUndo(NoteRectangle musicRectangle) {
        paneManager.addToPane(musicRectangle);
        paneManager.addToAll(musicRectangle);
    }
}
