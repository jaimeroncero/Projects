package tp1.p2.logic.gameobjects.plants;

import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class Peashooter extends Plant{

	public static final int COST = 50;
	public static final int ENDURANCE = 3;
	public static final int FREQUENCY = 1; 
	public static final int DAMAGE = 1;

	
	
	Peashooter(GameWorld game, int col, int row) {
		super(game, col, row);
	}
	
	public Peashooter(){
	}

	
	@Override
	public String getSymbol() {
		return Messages.PEASHOOTER_SYMBOL;
	}
	
	@Override
	protected String getNameAndShortcut() {
		return Messages.PEASHOOTER_NAME_SHORTCUT;
	}
	
	@Override
	public String getName() {
		return Messages.PEASHOOTER_NAME;
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
		if( this.contCycles % FREQUENCY == 0) {
			game.peashooterAttack(col, row, getDamage());
		}
		
		incrementContCycles();
	}


	@Override
	public Peashooter createNew(GameWorld game, int col, int row) {
		return new Peashooter(game, col, row);
	}
}
