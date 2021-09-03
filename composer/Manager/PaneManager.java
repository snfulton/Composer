package composer.Manager;

import composer.MouseEventHandler.PressEventHandler.PanePressHandler;
import composer.MouseEventHandler.PressEventHandler.PressEventHandler;
import composer.MusicRectangle.MusicRectangle;
import composer.RedLine;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class PaneManager {
    private final Collection<MusicRectangle> musicRectangleList = new HashSet<>();
    private MusicRectangle rectanglePressed;
    private Pane noteInputPane;
    private Rectangle dragSelectRectangle;
    private RedLine redLine;
    private final HashSet<MusicRectangle> selNoteSet = new HashSet<>();
    private final HashSet<MusicRectangle> prevSelNotes = new HashSet<>();
    private PressEventHandler pressEventHandler;

    public PaneManager(Pane pane){
        this.noteInputPane = pane;
        this.redLine = new RedLine();
        dragSelectRectangle = new Rectangle();
        noteInputPane.getChildren().add(dragSelectRectangle);
        dragSelectRectangle.setFill(Color.TRANSPARENT);
        dragSelectRectangle.setStroke(Color.web("e6d710"));
        dragSelectRectangle.setStrokeDashOffset(5);
        dragSelectRectangle.getStrokeDashArray().addAll(12d, 5d, 12d, 5d);
        pressEventHandler = new PanePressHandler(this);
    }

    public void setupRedLine() {
        redLine.setupRedLine();
    }

    public void stopRedLine(){
        redLine.stopRedLine(noteInputPane);
    }

    public void animateTimeline() {
        redLine.animateTimeline(noteInputPane, this.lastTick());
    }

    public int lastTick(){
        int lastTick = 0;
        for(MusicRectangle noteRect: musicRectangleList){
            if(lastTick < noteRect.getEndTick()){
                lastTick = noteRect.getEndTick();
            }
        }
        return lastTick;
    }

    public void removeDragSelectRect(){
        dragSelectRectangle.setStroke(Color.TRANSPARENT);
    }

    public void setUpDragSelectRect(int x, int y){
        dragSelectRectangle.setX(x);
        dragSelectRectangle.setY(y);
        dragSelectRectangle.setWidth(0);
        dragSelectRectangle.setHeight(0);
        dragSelectRectangle.setStroke(Color.web("e6d710"));
    }

    public void changeDragSelectX(double newX){
        dragSelectRectangle.setX(newX);
    }

    public void changeDragSelectY(double newY){
        dragSelectRectangle.setY(newY);
    }

    public void changeDragSelectSize(int newWidth, int newHeight){
        this.dragSelectRectangle.setWidth(newWidth);
        this.dragSelectRectangle.setHeight(newHeight);
    }

    public Rectangle getDragSelectRectangle() { return dragSelectRectangle; }

    public Iterator<MusicRectangle> allIterator(){
        return musicRectangleList.iterator();
    }

    public Iterator<MusicRectangle> selIterator(){
        return selNoteSet.iterator();
    }

    public HashSet<MusicRectangle> getSelNoteSet() { return selNoteSet; }

    public Iterator<MusicRectangle> prevSelIterator() {
        return prevSelNotes.iterator();
    }

    public void clearPane(){
        this.noteInputPane.getChildren().clear();
    }

    public void clearSel(){
        this.selNoteSet.clear();
    }

    public void clearSelSet(){
        selNoteSet.clear();
    }

    public void clearPrevSelNotes() { prevSelNotes.clear(); }

    public void removeSelFromAll(){
        musicRectangleList.removeAll(this.selNoteSet);
    }

    public void addToSel(Collection<MusicRectangle> newSel){
        selNoteSet.addAll(newSel);
    }
    public void addToSel(MusicRectangle newSel){
        selNoteSet.add(newSel);
    }

    public void moveSelNotesToPrevSelNotes() {prevSelNotes.clear(); prevSelNotes.addAll(selNoteSet);}

    public void addToPrevSel(Collection<MusicRectangle> prevSel) { prevSelNotes.addAll(prevSel);}

    public void addToAll(MusicRectangle musicRectangle){
        musicRectangleList.add(musicRectangle);
    }

    public void addToPane(MusicRectangle musicRectangle){
        noteInputPane.getChildren().addAll(musicRectangle);
    }

    public PressEventHandler getPressEventHandler(){
        return pressEventHandler;
    }

    public boolean isSelected(MusicRectangle musicRectangle){
        return selNoteSet.contains(musicRectangle);
    }

    public void removeFromPane(MusicRectangle musicRectangle){
        noteInputPane.getChildren().remove(musicRectangle);
    }

    public void removeFromAll(MusicRectangle musicRectangle){
        musicRectangleList.remove(musicRectangle);
    }

    public void addToAll(Collection<MusicRectangle> toAdd){
        musicRectangleList.addAll(toAdd);
    }

    public void removeFromSel(MusicRectangle musicRectangle){
        this.selNoteSet.remove(musicRectangle);
    }

    public void setPressEventHandler(PressEventHandler pressEventHandler){
        this.pressEventHandler = pressEventHandler;
    }

    public int selSize(){
        return selNoteSet.size();
    }

    public void clearAllCollections(){
        prevSelNotes.clear();
        musicRectangleList.clear();
        selNoteSet.clear();
    }

    public void resetPressHandler() {
        this.pressEventHandler = new PanePressHandler(this);
    }

    public void setPaneWidth(int paneWidth){
        this.noteInputPane.setMinWidth(paneWidth);
    }

    public int getPaneWidth(){
        return (int) this.noteInputPane.getWidth();
    }
}
