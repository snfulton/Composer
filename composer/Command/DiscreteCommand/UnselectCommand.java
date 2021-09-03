package composer.Command.DiscreteCommand;

import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

import java.util.Collection;
import java.util.HashSet;

public class UnselectCommand implements DiscreteCommand, Visitor, UndoVisitor {

    private Collection<MusicRectangle> unselectednotes;
    private PaneManager paneManager;

    public UnselectCommand(PaneManager paneManager, MusicRectangle musicRectangle){
        this.paneManager = paneManager;
        unselectednotes = new HashSet<>();
        this.unselectednotes.add(musicRectangle);
    }

    public UnselectCommand(PaneManager paneManager, Collection<MusicRectangle> unSelSet){
        this.unselectednotes = new HashSet<>();
        this.paneManager = paneManager;
        this.unselectednotes.addAll(unSelSet);
    }

    public void execute(){
        for(MusicRectangle musicRectangle : unselectednotes) {
            musicRectangle.accept(this);
        }
    }

    public void undo(){
        for(MusicRectangle musicRectangle : unselectednotes) {
            musicRectangle.acceptUndo(this);
        }
    }

    public void redo() {
        this.execute();
    }

    @Override
    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        currentMusicRectangle.getStyleClass().clear();
        currentMusicRectangle.getStyleClass().add("gesture-rectangle-outline");
    }

    @Override
    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        currentMusicRectangle.getStyleClass().clear();
        currentMusicRectangle.getStyleClass().add("note-rectangle-outline");
    }

    @Override
    public void visitGesture(GestureRectangle musicRectangle) {
        musicRectangle.getStyleClass().clear();
        musicRectangle.getStyleClass().add("gesture-rectangle-outline");
        paneManager.removeFromSel(musicRectangle);
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        musicRectangle.setDefaultStyle();
        paneManager.removeFromSel(musicRectangle);
    }

    @Override
    public void visitGestureUndo(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        currentMusicRectangle.getStyleClass().clear();
        currentMusicRectangle.getStyleClass().add("selected-gesture-rectangle-outline");
    }

    @Override
    public void visitNoteUndo(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        currentMusicRectangle.getStyleClass().clear();
        currentMusicRectangle.getStyleClass().add("selected-note-rectangle-outline");
    }

    @Override
    public void visitGestureUndo(GestureRectangle musicRectangle) {
        musicRectangle.getStyleClass().clear();
        musicRectangle.getStyleClass().add("selected-gesture-rectangle-outline");
        paneManager.addToSel(musicRectangle);
    }

    @Override
    public void visitNoteUndo(NoteRectangle musicRectangle) {
        musicRectangle.getStyleClass().clear();
        musicRectangle.getStyleClass().add("selected-note-rectangle-outline");
        paneManager.addToSel(musicRectangle);
    }
}
