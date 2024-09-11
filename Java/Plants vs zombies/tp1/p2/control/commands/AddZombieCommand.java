package tp1.p2.control.commands;

import tp1.p2.control.Command;
import tp1.p2.control.exceptions.CommandParseException;
import tp1.p2.control.exceptions.GameException;
import tp1.p2.logic.GameWorld;
import tp1.p2.logic.gameobjects.zombies.*;
import tp1.p2.view.Messages;

public class AddZombieCommand extends Command {

	private int zombieIdx;

	private int col;

	private int row;

	public AddZombieCommand() {
	}
	
	private AddZombieCommand(int zombieIdx, int col, int row) {
		this.zombieIdx = zombieIdx;
		this.col = col;
		this.row = row;
	}

	@Override
	protected String getName() {
		return Messages.COMMAND_ADD_ZOMBIE_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_ADD_ZOMBIE_SHORTCUT;
	}

	@Override
	public String getDetails() {
		return Messages.COMMAND_ADD_ZOMBIE_DETAILS;
	}

	@Override
	public String getHelp() {
		return Messages.COMMAND_ADD_ZOMBIE_HELP;
	}

	@Override
	public boolean execute(GameWorld game) throws GameException{
		if(ZombieFactory.isValidZombie(this.zombieIdx) 
				&& game.isValidPositionForAZombie(this.col, this.row)
				&& !game.isFullyOcuppied(col, row)) {
			
			Zombie zombie = ZombieFactory.spawnZombie(this.zombieIdx, game, this.col, this.row);
			game.addZombie(zombie);
			game.update();
			return true;
		}
		return false;
	}

	@Override
	public Command create(String[] parameters) throws GameException {
		if(parameters.length != 4) {
			throw new CommandParseException(Messages.COMMAND_PARAMETERS_MISSING);
		}
		else {
			try {
				return new AddZombieCommand(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]));
			}
			catch (NumberFormatException nfe) {
				throw new CommandParseException(Messages.INVALID_POSITION.formatted(parameters[1], parameters[2]), nfe);
			}
		}
	}


}
