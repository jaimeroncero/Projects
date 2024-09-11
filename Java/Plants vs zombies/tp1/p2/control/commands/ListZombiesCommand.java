package tp1.p2.control.commands;

import tp1.p2.control.Command;
import tp1.p2.control.exceptions.GameException;
import tp1.p2.logic.GameWorld;
import tp1.p2.logic.gameobjects.zombies.*;
import tp1.p2.view.Messages;

public class ListZombiesCommand extends Command {

	@Override
	protected String getName() {
		return Messages.COMMAND_LIST_ZOMBIES_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_LIST_ZOMBIES_SHORTCUT;
	}

	@Override
	public String getDetails() {
		return Messages.COMMAND_LIST_ZOMBIES_DETAILS;
	}

	@Override
	public String getHelp() {
		return Messages.COMMAND_LIST_ZOMBIES_HELP;
	}

	@Override
	public boolean execute(GameWorld game) throws GameException {
		StringBuilder buffer = new StringBuilder(Messages.AVAILABLE_ZOMBIES);

		for (Zombie z: ZombieFactory.getAvailableZombies()) {
			/* @formatter:off */
			buffer.append(Messages.LINE_SEPARATOR);
			buffer.append(z.getDescription());
			/* @formatter:on */
		}

		System.out.println(buffer.toString());
		System.out.append(Messages.LINE_SEPARATOR);

		return false;
	}

}
