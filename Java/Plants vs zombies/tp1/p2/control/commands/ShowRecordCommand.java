package tp1.p2.control.commands;

import tp1.p2.control.Command;
import tp1.p2.control.Level;
import tp1.p2.control.exceptions.CommandParseException;
import tp1.p2.control.exceptions.GameException;
import tp1.p2.logic.Record;
import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class ShowRecordCommand extends Command {
	
	private Record record;

	@Override
	protected String getName() {
		return Messages.COMMAND_SHOW_RECORD_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_SHOW_RECORD_SHORTCUT;
	}

	@Override
	public String getDetails() {
		return Messages.COMMAND_SHOW_RECORD_DETAILS;
	}

	@Override
	public String getHelp() {
		return Messages.COMMAND_SHOW_RECORD_HELP;
	}

	@Override
	public boolean execute(GameWorld game) throws GameException {
		this.record = game.getRecord();
		Level currentLevel = record.getLevel();
		System.out.println(Messages.CURRENT_RECORD.formatted(currentLevel.toString(),
				record.getScoreForLevel()));
		return false;
	}


}
