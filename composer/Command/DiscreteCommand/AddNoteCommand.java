package composer.Command.DiscreteCommand;

import composer.Constants;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.PressEventHandler.MusicRectanglePressHandler;
import composer.MusicRectangle.NoteRectangle;
import javafx.scene.paint.Paint;

public class AddNoteCommand implements DiscreteCommand {
    private NoteRectangle note;
    private int xCord;
    private int yCord;
    private Paint rectangleColor;
    private PaneManager paneManager;
    private int width;
    int channel;

    public AddNoteCommand(PaneManager paneManager, Paint rectangleColor, int channel, int xCord, int yCord, int width){
        this.rectangleColor = rectangleColor;
        this.paneManager = paneManager;
        this.yCord = yCord;
        this.xCord = xCord;
        this.channel = channel;
        this.width = width;
    }

    public void execute(){
        int rectY = (int) Math.floorDiv(yCord, 10)*10;
        this.note = new NoteRectangle(xCord, rectY,this.width, Constants.BAR_DISTANCE, channel);
        paneManager.addToAll(this.note);
        this.note.setFill(rectangleColor);
        this.note.setOnMousePressed(e->{
            MusicRectanglePressHandler pressEventHandler = new MusicRectanglePressHandler(this.paneManager);
            System.out.println("rect note press");
            pressEventHandler.setPressedMusicRect(this.note);
            pressEventHandler.setWasStartedOnSelNote(paneManager.isSelected(this.note));
            pressEventHandler.setWasStartedOnSelNote(paneManager.isSelected(this.note));
            pressEventHandler.setPressedMusicRect(this.note.getYoungestAncestor());
            pressEventHandler.setWasStartedOnNote(true);
            pressEventHandler.setWasStartedOnRightEdge((this.note.getRightX() - e.getX() <=5));
            paneManager.setPressEventHandler(pressEventHandler);
        });
        paneManager.addToPane(this.note);
//        SelectCommand selectCommand = new SelectCommand(paneManager, this.note);
//        selectCommand.execute();
    }
    public void undo(){
        DeleteCommand delNote = new DeleteCommand(this.paneManager, this.note);
        delNote.execute();
    }
    public void redo(){
        this.execute();
    }

    public NoteRectangle getNote(){
        return this.note;
    }
}
