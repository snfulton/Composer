package composer.Command;

import composer.SaveLoad.FromXML;

public interface Command {
    public void undo();
    public void redo() throws FromXML.XMLParsingException;
}
