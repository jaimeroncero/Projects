package tp1.p2.logic.gameobjects.plants;

import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class Sunflower extends Plant{
	public static final int COST = 20;
	public static final int ENDURANCE = 1;
	public static final int FREQUENCY = 3; 
	public static final int DAMAGE = 0;
	private static final int SUNCOINS_PRODUCED = 10;

	
	Sunflower(GameWorld game, int col, int row) {
		super(game, col, row);
	}
	
	public Sunflower(){
	}
	
	@Override
	public String getSymbol() {
		return Messages.SUNFLOWER_SYMBOL;
	}
	
	@Override
	protected String getNameAndShortcut() {
		return Messages.SUNFLOWER_NAME_SHORTCUT;
	}
	
	@Override
	public String getName() {
		return Messages.SUNFLOWER_NAME;
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
	public void update() {

		if(this.contCycles != 0 && this.contCycles % FREQUENCY == 0) {
			game.addSun(SUNCOINS_PRODUCED);
		}
		
		incrementContCycles();
	}


	@Override
	public Plant createNew(GameWorld game, int col, int row) {
		return new Sunflower(game,col, row);
	}
	
}
