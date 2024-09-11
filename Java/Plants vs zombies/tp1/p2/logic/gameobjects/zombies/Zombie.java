package tp1.p2.logic.gameobjects.zombies;

import tp1.p2.logic.GameWorld;
import tp1.p2.logic.gameobjects.GameObject;
import tp1.p2.view.Messages;

public class Zombie extends GameObject{
	protected static final int ENDURANCE = 5;
	protected static final int DAMAGE = 1;
	protected static final int SPEED = 2; 
	

	
	Zombie(GameWorld game, int col, int row) {
		super(game, col, row);
	}
	
	public Zombie(){
	}
	
	@Override
	public String getSymbol() {
		return Messages.ZOMBIE_SYMBOL;
	}
	
	@Override
	public String getDescription() {
		return Messages.zombieDescription(getName(), getSpeed(), getDamage(), getEndurance());
	}
	
	
	protected int getSpeed() {
		return SPEED;
	}
	
	@Override
	protected int getDamage() {
		return DAMAGE;
	}
	
	@Override
	public int getEndurance() {
		return ENDURANCE;
	}

	@Override
	public String getName() {
		return Messages.ZOMBIE_NAME;
	}

	@Override
	public boolean receiveZombieAttack(int damage) {
		return false;
	}
	
	@Override
	public boolean receivePlantAttack(int damage) {
		this.lifePoints -= damage;
		return true;
	}
	

	@Override
	public void update() {
		
		boolean isEmpty = !game.isFullyOcuppied(this.col - 1, this.row);
		
		
		if(this.contCycles != 0 && this.contCycles % getSpeed() == 0 && isEmpty) {
			--this.col;
		}
		
		isEmpty = !game.isFullyOcuppied(this.col - 1, this.row);
		
		if(!isEmpty) {
			game.getFillingObjectInPosition(this.col - 1, this.row).receiveZombieAttack(getDamage());
		}

		
		incrementContCycles();
	}

	@Override
	public void onEnter() {
		game.incrementZombiesAlived();
		game.decrementRemainingZombies();
		
	}

	@Override
	public void onExit() {
		game.decrementZombiesAlived();
		game.incrementScore();
	}
	
	@Override
	public Zombie createNew(GameWorld game, int col, int row) {
		return new Zombie(game, col, row);
	}
	
	@Override
	public boolean fillPosition() {
		return true;
	}
	
	@Override
	public boolean receivePlantBombAttack(int damage) {
		this.lifePoints -= damage;
		if(!this.isAlive()) game.incrementScore();
		return true;
	}

}
