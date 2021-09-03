package composer.Manager;
import composer.Command.Command;
import composer.SaveLoad.FromXML;

import java.util.Stack;

public class CommandManager {

    private final Stack<Command> redo = new Stack<>();
    private final Stack<Command> undo = new Stack<>();

    public boolean isRedoEmpty(){
        if (redo.isEmpty()){
            return true;
        }
        return false;
    }

    public boolean isUndoEmpty(){
        if (undo.isEmpty()){
            return true;
        }
        return false;
    }

    public void redo() throws FromXML.XMLParsingException {
        if(redo.isEmpty()){
            return;
        }
        Command command = redo.pop();
        undo.push(command);
        command.redo();
    }

    public void undo(){
        if(undo.isEmpty()){
            return;
        }
        Command command = undo.pop();
        redo.push(command);
        command.undo();
    }

    public void addCommand(Command command){
        redo.clear();
        undo.push(command);
    }

    public int redoSize(){
        return redo.size();
    }

    public int undoSize(){
        return undo.size();
    }

    public void clear(){
        this.undo.clear();
        this.redo.clear();
    }

}
