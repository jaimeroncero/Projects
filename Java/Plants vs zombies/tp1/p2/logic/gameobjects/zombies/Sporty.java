package tp1.p2.logic.gameobjects.zombies;

import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class Sporty extends Zombie{
	
	protected static final int SPEED = 1; 
	protected static final int ENDURANCE = 2;

	Sporty(GameWorld game, int col, int row){
		super(game, col, row);
	}
	
	public Sporty() {
	}

	@Override
	public String getSymbol() {
		return Messages.SPORTY_ZOMBIE_SYMBOL;
	}
	
	@Override
	protected int getSpeed() {
		return SPEED;
	}
	
	@Override
	public int getEndurance() {
		return ENDURANCE;
	}
	
	@Override
	public String getName() {
		return Messages.SPORTY_ZOMBIE_NAME;
	}
	
	@Override
	public Sporty createNew(GameWorld game, int col, int row) {
		return new Sporty(game, col, row);
	}
}
