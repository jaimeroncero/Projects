package tp1.p2.control.commands;

import java.io.IOException;

import tp1.p2.control.Command;
import tp1.p2.control.Level;
import tp1.p2.control.exceptions.CommandParseException;
import tp1.p2.control.exceptions.GameException;
import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class ResetCommand extends Command {

	private Level level;

	private long seed;

	public ResetCommand() {
	}

	public ResetCommand(Level level, long seed) {
		this.level = level;
		this.seed = seed;
	}

	@Override
	protected String getName() {
		return Messages.COMMAND_RESET_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_RESET_SHORTCUT;
	}

	@Override
	public String getDetails() {
		return Messages.COMMAND_RESET_DETAILS;
	}

	@Override
	public String getHelp() {
		return Messages.COMMAND_RESET_HELP;
	}

	@Override
	public boolean execute(GameWorld game) throws GameException, IOException {
		if(this.level == null) {
			game.reset();
		}
		
		else game.reset(level, seed);
		
		return true;
	}

	@Override
	public Command create(String[] parameters) throws GameException {
		if(parameters.length == 1) {
			return new ResetCommand();
		}
		
		else if (parameters.length == 3) {
			
			Level levelParam = Level.valueOfIgnoreCase(parameters[1]);
			if(levelParam == null) {
				throw new CommandParseException(Messages.INVALID_COMMAND);
			}
			
			Long seedParam = null;
			try {
				seedParam = Long.parseLong(parameters[2]);
				this.level = levelParam;
				this.seed = seedParam;
				return new ResetCommand(this.level, this.seed);
			}
			catch(NumberFormatException nfe) {
				throw new CommandParseException(Messages.INVALID_COMMAND);
			}
		}
		else {
			throw new CommandParseException(Messages.COMMAND_PARAMETERS_MISSING);
		}
	}
}
