package composer.MusicRectangle;

import composer.BooleanChecker.BooleanChecker;
import composer.Command.UndoVisitor;
import composer.Command.Visitor;
import composer.Constants;
import javafx.scene.DepthTest;
import javafx.scene.shape.Rectangle;

import java.util.Iterator;

public abstract class MusicRectangle extends Rectangle {

    private int channel;
    protected MusicRectangle youngestAncestor;

    public MusicRectangle(int x, int y, int width, int height, int channel){
        super(x,y,width,height);
        this.channel = channel;
        this.setDepthTest(DepthTest.DISABLE);
        this.youngestAncestor = this;
    }

    public int getStartTick() {
        return (int) this.getX()* Constants.TICKS_PER_PIXEL;
    }
    public int getEndTick() {
        return (int) this.getRightX()* Constants.TICKS_PER_PIXEL;
    }
    public int getBottomY(){return (int)(this.getY()+this.getHeight()); }
    public int getRightX(){ return (int)(this.getX()+this.getWidth()); }
    public int getDurationTick() {
        return (int) (this.getWidth() * Constants.TICKS_PER_PIXEL);
    }
    public int getChannel(){
        return this.channel;
    }
    public void setYoungestAncestor(MusicRectangle musicRectangle){
        this.youngestAncestor = musicRectangle;
    };
    public MusicRectangle getYoungestAncestor(){
        return this.youngestAncestor;
    }

    abstract public boolean isTrueForAll(BooleanChecker booleanChecker);
    abstract public boolean isTrueForOne(BooleanChecker booleanChecker);
    abstract public void accept(Visitor command);
    abstract public void accept(Visitor command, GestureRectangle parentMusicRectangle);
    abstract public void acceptUndo(UndoVisitor command);
    abstract public void acceptUndo(UndoVisitor command, GestureRectangle parentMusicRectangle);
}
