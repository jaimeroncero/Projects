package tp1.p2.control.commands;

import tp1.p2.control.Command;
import tp1.p2.control.exceptions.GameException;
import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class HelpCommand extends Command {

	@Override
	protected String getName() {
		return Messages.COMMAND_HELP_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_HELP_SHORTCUT;
	}

	@Override
	public String getDetails() {
		return Messages.COMMAND_HELP_DETAILS;
	}

	@Override
	public String getHelp() {
		return Messages.COMMAND_HELP_HELP;
	}

	@Override
	public boolean execute(GameWorld game) throws GameException {
		StringBuilder buffer = new StringBuilder(Messages.HELP_AVAILABLE_COMMANDS);
		buffer.append(Messages.LINE_SEPARATOR);
		for (Command command : Command.getAvailableCommands()) {
			/* @formatter:off */
			buffer.append(command.getDetails())
			.append(Messages.HELP_DETAILS_COMMAND_HELP_SEPARATOR)
			.append(command.getHelp())
			.append(Messages.LINE_SEPARATOR);
			/* @formatter:on */
		}

		System.out.print(buffer.toString());

		return false;
	}

}
