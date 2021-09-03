package composer.Command.DiscreteCommand;

import composer.Command.Command;
import composer.SaveLoad.FromXML;

public interface DiscreteCommand extends Command {
    public void execute() throws FromXML.XMLParsingException;
}
