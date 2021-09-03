package composer.Manager;

import composer.SaveLoad.toXML;
import composer.MusicRectangle.MusicRectangle;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;


public class ClipboardManager{

    final Clipboard clipboard = Clipboard.getSystemClipboard();

    public void addToClipboard(Collection<MusicRectangle> toBoard) throws
            ParserConfigurationException, TransformerException {
        ClipboardContent content = new ClipboardContent();
        toXML xmlParser = new toXML(toBoard);
        Document xmlDoc = xmlParser.convert();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
        String parsedtoString = writer.getBuffer().toString();
        content.putString(parsedtoString);
        clipboard.setContent(content);
    }

    public Document getClipboardContent() throws ParserConfigurationException, SAXException, IOException {
        if (clipboard.getContent(DataFormat.PLAIN_TEXT) == null) {
            return null;
        } else {
            String toConvert = clipboard.getString();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document toReturn = docBuilder.parse(new InputSource(new StringReader(toConvert)));
            return toReturn;
        }
    }

    public void clearClipboard() {
        ClipboardContent content = new ClipboardContent();
        content.putString("");
        clipboard.setContent(content);
        clipboard.clear();
    }

    public boolean isEmpty(){
        return this.clipboard.hasString();
    }

}
