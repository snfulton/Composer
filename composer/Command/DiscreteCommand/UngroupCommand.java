package composer.Command.DiscreteCommand;

import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

import java.util.Collection;
import java.util.HashSet;

public class UngroupCommand implements DiscreteCommand, Visitor, UndoVisitor {

    private Collection<MusicRectangle> ungroupedNotes;
    private PaneManager paneManager;
    private MusicRectangle gestureToUngroup;

    public UngroupCommand(PaneManager paneManager, MusicRectangle gestureToUngroup){
        this.gestureToUngroup = gestureToUngroup;
        this.paneManager = paneManager;
        ungroupedNotes = new HashSet<>();
    }

    public void execute(){
        gestureToUngroup.accept(this);
    }

    public void undo(){
        for(MusicRectangle musicRectangle: ungroupedNotes){
            musicRectangle.acceptUndo(this);
        }
        paneManager.addToPane(gestureToUngroup);
        paneManager.addToAll(gestureToUngroup);
    }

    public void redo(){
        this.execute();
    }

    @Override
    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        if(parentMusicRectangle == gestureToUngroup){
            currentMusicRectangle.setYoungestAncestor(currentMusicRectangle);
            SetYACommand setYACommand = new SetYACommand(currentMusicRectangle);
            setYACommand.execute();
            paneManager.addToAll(currentMusicRectangle);
            ungroupedNotes.add(currentMusicRectangle);
        }
    }

    @Override
    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        if(parentMusicRectangle == gestureToUngroup){
            System.out.println("parenting");
            SetYACommand setYACommand = new SetYACommand(currentMusicRectangle);
            setYACommand.execute();
            paneManager.addToAll(currentMusicRectangle);
            ungroupedNotes.add(currentMusicRectangle);
        }
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        return;
    }

    @Override
    public void visitGesture(GestureRectangle musicRectangle) {
        paneManager.removeFromAll(musicRectangle);
        paneManager.removeFromPane(musicRectangle);
        return;
    }

    @Override
    public void visitGestureUndo(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        currentMusicRectangle.setYoungestAncestor(this.gestureToUngroup);
    }

    @Override
    public void visitNoteUndo(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        currentMusicRectangle.setYoungestAncestor(this.gestureToUngroup);
    }

    @Override
    public void visitGestureUndo(GestureRectangle musicRectangle) {
        musicRectangle.setYoungestAncestor(this.gestureToUngroup);
        paneManager.removeFromAll(musicRectangle);
    }

    @Override
    public void visitNoteUndo(NoteRectangle musicRectangle) {
        musicRectangle.setYoungestAncestor(this.gestureToUngroup);
        paneManager.removeFromAll(musicRectangle);
    }
}
