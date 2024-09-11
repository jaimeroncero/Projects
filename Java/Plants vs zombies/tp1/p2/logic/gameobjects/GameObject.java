package tp1.p2.logic.gameobjects;


import static tp1.p2.view.Messages.status;

import tp1.p2.logic.GameItem;
import tp1.p2.logic.GameWorld;

/**
 * Base class for game non playable character in the game.
 *
 */
public abstract class GameObject implements GameItem {

	protected GameWorld game;

	protected int col;

	protected int row;
	
	protected int lifePoints;
	
	protected int contCycles;


	protected GameObject() {
	}

	protected GameObject(GameWorld game, int col, int row) {
		this.game = game;
		this.col = col;
		this.row = row;
		this.lifePoints = getEndurance();
		this.contCycles = 0;
		
	}

	public boolean isInPosition(int col, int row) {
		return this.col == col && this.row == row;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}
	
	public boolean isAlive() {
		return lifePoints > 0;
	}

	public String toString() {
		if (isAlive()) {
			return status(getSymbol(), lifePoints);
		} else {
			return "";
		}
	}
	
	protected void incrementContCycles() {
		this.contCycles ++;
	}
	
	@Override
	public boolean receivePlantBombAttack(int damage) {
		return this.receivePlantAttack(damage);
	}
	

	@Override
	public boolean catchObject() {
		return false;
	}

	public abstract String getSymbol();

	abstract public String getDescription();
	
	public abstract String getName();

	abstract public void update();
	
	abstract public void onEnter();
	
	abstract public void onExit();
	
	abstract protected int getEndurance();
	
	protected abstract int getDamage();
	
	public abstract GameObject createNew(GameWorld game, int col, int row);

	public abstract boolean fillPosition();
	
	
}
