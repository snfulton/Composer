package composer.Command;

import composer.MusicRectangle.MusicRectangle;
import composer.SaveLoad.FromXML;

import java.util.*;

public class CommandWrapper implements Command{
    private final List<Command> commandList = new ArrayList<>();

    public void add(Command command){
        commandList.add(command);
    }

    @Override
    public void undo() {
        ListIterator<Command> li = commandList.listIterator(commandList.size());

// Iterate in reverse.
        while(li.hasPrevious()) {
            li.previous().undo();
        }
    }

    @Override
    public void redo() throws FromXML.XMLParsingException {
        Iterator<Command> it = commandList.iterator();
        while(it.hasNext()){
            it.next().redo();
        }
    }
}
