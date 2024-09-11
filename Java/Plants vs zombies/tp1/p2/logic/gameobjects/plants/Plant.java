package tp1.p2.logic.gameobjects.plants;

import tp1.p2.logic.GameWorld;
import tp1.p2.logic.gameobjects.GameObject;
import tp1.p2.view.Messages;

public abstract class Plant extends GameObject {

	Plant(GameWorld game, int col, int row) {
		super(game, col, row);
	}
	
	Plant() {
	}
	
	@Override
	public String getDescription() {
		return Messages.plantDescription(getNameAndShortcut(), getCost(), getDamage(), getEndurance());
	}

	
	@Override
	public boolean receiveZombieAttack(int damage) {
		this.lifePoints -= damage;
		return true;
	}
	
	@Override
	public boolean receivePlantAttack(int damage) {
		return false;
	}
	
	public abstract int getCost();
	
	protected abstract String getNameAndShortcut();
	
	@Override
	public abstract Plant createNew(GameWorld game, int col, int row);
	

	@Override
	public void onEnter() {
		
	}
	
	@Override
	public void onExit() {

	}
	
	@Override
	public boolean fillPosition() {
		return true;
	}
	



	


	

}