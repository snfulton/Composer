package composer.SaveLoad;

import composer.Command.DiscreteCommand.AddNoteCommand;
import composer.Command.DiscreteCommand.GroupCommand;
import composer.Manager.PaneManager;
import composer.MusicRectangle.MusicRectangle;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.HashSet;

public class FromXML {
    private PaneManager paneManager;
    private Collection<MusicRectangle> toReturn;

    public class XMLParsingException extends Exception{

    }

    public FromXML(PaneManager paneManager){
        this.paneManager = paneManager;
    }

    public Collection<MusicRectangle> convert(Document doc, int xOffset, int yOffset) throws XMLParsingException{
        toReturn = new HashSet<>();
        System.out.println("working");
        Element root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for(int i = 0; i< nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            toReturn.add(recursiveCreator(element, xOffset, yOffset));
        }
        return toReturn;
    }

    private MusicRectangle recursiveCreator(Element element, int offSetX, int offsetY) throws XMLParsingException {
        MusicRectangle recursToReturn;
        int x;
        int y;
        int height;
        int width;
        int channel;
        try{
            x = (int) Double.parseDouble(element.getAttribute("X"))+ offSetX;
            y = (int) Double.parseDouble(element.getAttribute("Y")) + offsetY;
            height = (int) Double.parseDouble(element.getAttribute("height"));
            width = (int) Double.parseDouble(element.getAttribute("width"));
            channel = (int) Double.parseDouble(element.getAttribute("channel"));
        }catch(NumberFormatException e){
            throw new XMLParsingException();
        }
        if(element.getTagName() == "gesture"){
            if(!element.hasChildNodes()){
                throw new XMLParsingException();
            }
            Collection<MusicRectangle> children = new HashSet<>();
            NodeList nodeList = element.getChildNodes();
            for(int i = 0; i< nodeList.getLength(); i++){
                Element child = (Element) nodeList.item(i);
                children.add(recursiveCreator(child, offSetX, offsetY));
            }
            GroupCommand group = new GroupCommand(this.paneManager, children);
            group.execute();
//            this.paneManager.addToAll(group.getGesture());
            recursToReturn = group.getGesture();
        }
        else if(element.getTagName() == "note"){
            Color color = Color.valueOf(element.getAttribute("color"));
            if(element.hasChildNodes()){
                throw new XMLParsingException();
            }
            AddNoteCommand note = new AddNoteCommand(this.paneManager, color, channel, x, y, width);
            note.execute();
            recursToReturn = note.getNote();
        }else{
            throw new XMLParsingException();
        }
        return recursToReturn;
    }
}
