package composer.Command.DiscreteCommand;

import composer.Command.Visitor;
import composer.Constants;
import composer.MidiPlayer;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;

import java.util.Collection;
import java.util.HashSet;

public class AddToPlayer implements DiscreteCommand, Visitor {

    private MidiPlayer player;
    private Collection<MusicRectangle> toAdd;

    public AddToPlayer(MidiPlayer player, MusicRectangle toAdd){
        this.player = player;
        this.toAdd = new HashSet<>();
        this.toAdd.add(toAdd);
    }

    public AddToPlayer(MidiPlayer player, Collection<MusicRectangle> toAdd){
        this.player = player;
        this.toAdd = toAdd;
    }

    @Override
    public void undo() {
        this.player.clear();
    }

    @Override
    public void redo() {
        this.execute();
    }

    @Override
    public void execute() {
        for(MusicRectangle musicRectangle: toAdd){
            musicRectangle.accept(this);
        }
    }

    @Override
    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle) {
        return;
    }

    @Override
    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle) {
        player.addNote(Constants.calculatePitch((int)currentMusicRectangle.getY()), Constants.VOLUME, currentMusicRectangle.getStartTick(), currentMusicRectangle.getDurationTick(),
                currentMusicRectangle.getChannel(), 0);
    }

    @Override
    public void visitGesture(GestureRectangle musicRectangle) {
        return;
    }

    @Override
    public void visitNote(NoteRectangle musicRectangle) {
        player.addNote(Constants.calculatePitch((int)musicRectangle.getY()), Constants.VOLUME, musicRectangle.getStartTick(), musicRectangle.getDurationTick(),
                musicRectangle.getChannel(), 0);
    }
}
