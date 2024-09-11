package tp1.p2.logic.gameobjects.plants;

import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class WallNut extends Plant{
	
	private static final int COST = 50;
	private static final int DAMAGE = 0;
	private static final int ENDURANCE = 10;
	
	WallNut(GameWorld game, int col, int row) {
		super(game, col, row);
	}
	
	public WallNut(){
	}
	

	@Override
	public int getCost() {
		return COST;
	}

	@Override
	protected int getDamage() {
		return DAMAGE;
	}

	@Override
	protected int getEndurance() {
		return ENDURANCE;
	}

	@Override
	public String getName() {
		return Messages.WALL_NUT_NAME;
	}
	
	@Override
	protected String getNameAndShortcut() {
		return Messages.WALL_NUT_NAME_SHORTCUT;
	}

	@Override
	public String getSymbol() {
		return Messages.WALL_NUT_SYMBOL;
	}

	@Override
	public void update() {
		incrementContCycles();
	}


	@Override
	public WallNut createNew(GameWorld game, int col, int row) {
		return new WallNut(game, col, row);
	}

}
