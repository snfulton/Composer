package composer.Command.DiscreteCommand;

import composer.Manager.PaneManager;
import composer.MusicRectangle.MusicRectangle;
import composer.SaveLoad.FromXML;
import javafx.scene.layout.Pane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Collection;
import java.util.HashSet;

public class PasteCommand implements DiscreteCommand{
    private PaneManager paneManager;
    private Document clipboardContents;
    private Collection<MusicRectangle> toPaste;
    private FromXML fromXML;

    public PasteCommand(PaneManager paneManager, Document clipboardContents){
        this.paneManager = paneManager;
        this.clipboardContents = clipboardContents;
        this.fromXML = new FromXML(this.paneManager);
        this.toPaste = new HashSet<>();
    }

    public void execute() throws FromXML.XMLParsingException {
        toPaste = this.fromXML.convert(this.clipboardContents, 50, 50);
    }

    public void undo(){
        DeleteCommand deleteCommand = new DeleteCommand(this.paneManager, this.toPaste);
        deleteCommand.execute();
    }

    public void redo() throws FromXML.XMLParsingException {
        this.execute();
    }
}
