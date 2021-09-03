package composer.Command.DiscreteCommand;

import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

import java.util.Collection;
import java.util.HashSet;

public class SelectCommand implements DiscreteCommand, Visitor, UndoVisitor {

    private Collection<MusicRectangle> selectednotes = new HashSet<>();
    private PaneManager paneManager;

    public SelectCommand(PaneManager paneManager, Collection<MusicRectangle> selNoteSet){
        selectednotes.addAll(selNoteSet);
        this.paneManager = paneManager;
    }

    public SelectCommand(PaneManager paneManager, MusicRectangle musicRectangle){
        this.paneManager = paneManager;
        selectednotes.add(musicRectangle);
    }

    public void execute(){
        for(MusicRectangle musicRectangle : selectednotes){
            musicRectangle.accept(this);
        }
    }

    public void undo(){
        for(MusicRectangle musicRectangle : selectednotes){
            musicRectangle.acceptUndo(this);
        }
    }

    public void redo(){
        this.execute();
    }

    @Override
    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        currentMusicRectangle.getStyleClass().clear();
        currentMusicRectangle.getStyleClass().add("selected-gesture-rectangle-outline");
    }

    @Override
    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        currentMusicRectangle.getStyleClass().clear();
        currentMusicRectangle.getStyleClass().add("selected-note-rectangle-outline");
    }

    @Override
    public void visitGesture(GestureRectangle musicRectangle) {
        musicRectangle.getStyleClass().clear();
        musicRectangle.getStyleClass().add("selected-gesture-rectangle-outline");
        paneManager.addToSel(musicRectangle);
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        musicRectangle.getStyleClass().clear();
        musicRectangle.getStyleClass().add("selected-note-rectangle-outline");
        paneManager.addToSel(musicRectangle);
    }

    @Override
    public void visitGestureUndo(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        currentMusicRectangle.getStyleClass().clear();
        currentMusicRectangle.getStyleClass().add("gesture-rectangle-outline");
    }

    @Override
    public void visitNoteUndo(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        currentMusicRectangle.getStyleClass().clear();
        currentMusicRectangle.getStyleClass().add("note-rectangle-outline");
    }

    @Override
    public void visitGestureUndo(GestureRectangle musicRectangle) {
        paneManager.removeFromSel(musicRectangle);
        musicRectangle.getStyleClass().clear();
        musicRectangle.getStyleClass().add("gesture-rectangle-outline");
    }

    @Override
    public void visitNoteUndo(NoteRectangle musicRectangle) {
        paneManager.removeFromSel(musicRectangle);
        musicRectangle.getStyleClass().clear();
        musicRectangle.getStyleClass().add("note-rectangle-outline");
    }
}
