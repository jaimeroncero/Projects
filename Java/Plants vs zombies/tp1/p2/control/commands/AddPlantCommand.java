package tp1.p2.control.commands;

import tp1.p2.control.Command;
import tp1.p2.control.exceptions.*;
import tp1.p2.logic.GameWorld;
import tp1.p2.logic.gameobjects.plants.*;
import tp1.p2.view.Messages;

public class AddPlantCommand extends Command implements Cloneable {

	protected int col;

	protected int row;

	protected String plantName;

	protected boolean consumeCoins;

	public AddPlantCommand() {
		this(true);
	}

	public AddPlantCommand(int col, int row, String plantName, boolean consumeCoins) {
		this.col = col;
		this.row = row;
		this.plantName = plantName;
		this.consumeCoins = consumeCoins;
	}
	
	public AddPlantCommand(boolean consumeCoins) {
		this.consumeCoins = consumeCoins;
	}

	@Override
	protected String getName() {
		return Messages.COMMAND_ADD_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_ADD_SHORTCUT;
	}

	@Override
	public String getDetails() {
		return Messages.COMMAND_ADD_DETAILS;
	}

	@Override
	public String getHelp() {
		return Messages.COMMAND_ADD_HELP;
	}


	@Override
	public boolean execute(GameWorld game) throws GameException {
		if(game.isValidPosition(col, row) && !game.isFullyOcuppied(col, row)) {
			
			if(consumeCoins) {
				Plant p = PlantFactory.spawnPlant(plantName, game, col, row);
				
				if(game.addPlant(p)) {
					game.update();
					return true;
				}
				throw new NotEnoughCoinsException(Messages.NOT_ENOUGH_COINS);
			}
			
			game.addGameObject(PlantFactory.spawnPlant(plantName, game, col, row));
			game.update();
			return true;
		}
		throw new InvalidPositionException(Messages.INVALID_POSITION.formatted(col, row));
	}

	@Override
	public Command create(String[] parameters) throws GameException {
		if(parameters.length == 4) {
			if(PlantFactory.isValidPlant(parameters[1])){
				try {
					return new AddPlantCommand(Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]), parameters[1], consumeCoins);
				}
				catch(NumberFormatException nfe) {
					throw new CommandParseException(Messages.INVALID_POSITION.formatted(parameters[2], parameters[3]), nfe);
				}
			}
			else throw new CommandExecuteException(Messages.INVALID_GAME_OBJECT);
		}
		else throw new CommandParseException(Messages.COMMAND_PARAMETERS_MISSING);
	}
}


