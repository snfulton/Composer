package composer.SaveLoad;

import composer.Command.Visitor;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

public class toXML implements Visitor {

    private Collection<MusicRectangle> musicRectangleCollection;
    private Document document;

    public toXML(Collection<MusicRectangle> toConvert) throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        this.document = documentBuilder.newDocument();
        this.musicRectangleCollection = toConvert;
    }

    public Document convert(){
        Element composition = document.createElement("composition");
        composition.setAttribute("ID", "comp");
        composition.setIdAttribute("ID", true);
        document.appendChild(composition);
        for(MusicRectangle musicRectangle : musicRectangleCollection){
            musicRectangle.accept(this);
        }
        return document;
    }

    public void visitGesture(GestureRectangle parentMusicRectangle, GestureRectangle currentMusicRectangle){
        Element gesture = document.createElement("gesture");
        gesture.setAttribute("ID", Integer.toString(currentMusicRectangle.hashCode()));
        gesture.setIdAttribute("ID", true);
        gesture.setAttribute("X", Integer.toString((int) currentMusicRectangle.getX()));
        gesture.setAttribute("Y", Integer.toString((int) currentMusicRectangle.getY()));
        gesture.setAttribute("start-tick", Integer.toString(currentMusicRectangle.getStartTick()));
        gesture.setAttribute("end-tick", Integer.toString(currentMusicRectangle.getEndTick()));
        gesture.setAttribute("width", Integer.toString((int) currentMusicRectangle.getWidth()));
        gesture.setAttribute("height", Integer.toString((int) currentMusicRectangle.getHeight()));
        gesture.setAttribute("channel", Integer.toString(currentMusicRectangle.getChannel()));
        Element parent = document.getElementById(Integer.toString(parentMusicRectangle.hashCode()));
        parent.appendChild(gesture);
    }

    public void visitNote(GestureRectangle parentMusicRectangle, NoteRectangle currentMusicRectangle){
        Element note = document.createElement("note");
        note.setAttribute("X", Double.toString(currentMusicRectangle.getX()));
        note.setAttribute("Y", Double.toString(currentMusicRectangle.getY()));
        note.setAttribute("start-tick", Integer.toString(currentMusicRectangle.getStartTick()));
        note.setAttribute("end-tick", Integer.toString(currentMusicRectangle.getEndTick()));
        note.setAttribute("width", Double.toString(currentMusicRectangle.getWidth()));
        note.setAttribute("height", Double.toString(currentMusicRectangle.getHeight()));
        note.setAttribute("channel", Integer.toString(currentMusicRectangle.getChannel()));
        note.setAttribute("color", currentMusicRectangle.getFill().toString());
        Element parent = document.getElementById(Integer.toString(parentMusicRectangle.hashCode()));
        parent.appendChild(note);
    }
    public void visitGesture(GestureRectangle musicRectangle){
        Element gesture = document.createElement("gesture");
        gesture.setAttribute("ID", Integer.toString(musicRectangle.hashCode()));
        gesture.setIdAttribute("ID", true);
        gesture.setAttribute("X", Double.toString(musicRectangle.getX()));
        gesture.setAttribute("Y", Double.toString(musicRectangle.getY()));
        gesture.setAttribute("start-tick", Integer.toString(musicRectangle.getStartTick()));
        gesture.setAttribute("end-tick", Integer.toString(musicRectangle.getEndTick()));
        gesture.setAttribute("width", Double.toString(musicRectangle.getWidth()));
        gesture.setAttribute("height", Double.toString(musicRectangle.getHeight()));
        gesture.setAttribute("channel", Integer.toString(musicRectangle.getChannel()));
        Element parent = document.getElementById("comp");
        parent.appendChild(gesture);
    }
    public void visitNote(NoteRectangle musicRectangle){
        Element note = document.createElement("note");
        note.setAttribute("X", Double.toString(musicRectangle.getX()));
        note.setAttribute("Y", Double.toString(musicRectangle.getY()));
        note.setAttribute("start-tick", Integer.toString(musicRectangle.getStartTick()));
        note.setAttribute("end-tick", Integer.toString(musicRectangle.getEndTick()));
        note.setAttribute("width", Double.toString(musicRectangle.getWidth()));
        note.setAttribute("height", Double.toString(musicRectangle.getHeight()));
        note.setAttribute("channel", Integer.toString(musicRectangle.getChannel()));
        note.setAttribute("color", musicRectangle.getFill().toString());
        Element parent = document.getElementById("comp");
        parent.appendChild(note);
    }


}
