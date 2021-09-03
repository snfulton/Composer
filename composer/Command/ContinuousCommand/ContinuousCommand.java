package composer.Command.ContinuousCommand;

import composer.Command.Command;

public interface ContinuousCommand extends Command {
    public void execute(int currX, int currY);
}
