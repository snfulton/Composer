package composer.SaveLoad;

import composer.Command.DiscreteCommand.AddNoteCommand;
import composer.Command.DiscreteCommand.GroupCommand;
import composer.Constants;
import composer.Manager.PaneManager;
import composer.MusicRectangle.MusicRectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javafx.scene.paint.Color;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class XMLLoader implements Loader{
    private Document doc;
    private Collection<MusicRectangle> toReturn;
    private PaneManager paneManager;
    private FromXML fromXML;


    public XMLLoader(File file, PaneManager paneManager) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(file);
        toReturn = new HashSet<>();
        this.paneManager = paneManager;
        this.fromXML = new FromXML(this.paneManager);
    }

    @Override
    public void load() throws FromXML.XMLParsingException {
        toReturn = this.fromXML.convert(this.doc, 0, 0);
    }

}
