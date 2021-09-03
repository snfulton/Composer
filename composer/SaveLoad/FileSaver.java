package composer.SaveLoad;

import composer.Command.Visitor;
import composer.Manager.PaneManager;
import composer.MusicRectangle.GestureRectangle;
import composer.MusicRectangle.MusicRectangle;
import composer.MusicRectangle.NoteRectangle;
import javafx.scene.paint.Paint;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.plaf.multi.MultiDesktopIconUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.paint.Color;


public class FileSaver{
    private static File xmlFilePath;
    private HashSet<MusicRectangle> toSave;
    private toXML XMLparser;
    /**
     * Saves compositions to XML by parsing them with a DOM parser.
     * @param filePath The file to create and save the XML to
     * @param paneManager A reference to the paneManager in order to access the note list.
     * @throws ParserConfigurationException
     */
    public FileSaver(File filePath, PaneManager paneManager) throws ParserConfigurationException {
        Iterator<MusicRectangle> it = paneManager.allIterator();
        this.toSave = new HashSet<MusicRectangle>();
        while (it.hasNext()){
            toSave.add(it.next());
        }
        this.xmlFilePath = filePath;
        this.XMLparser = new toXML(toSave);
    }

    public File getSaveFile(){return xmlFilePath;}

    /**
     * Save a XML document.
     */
    public void Save() throws TransformerException {
        Document document = XMLparser.convert();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(xmlFilePath);
        transformer.transform(domSource, streamResult);
    }
}

