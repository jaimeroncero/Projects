package tp1.p2.logic.gameobjects.plants;

import tp1.p2.logic.GameWorld;
import tp1.p2.logic.actions.ExplosionAction;
import tp1.p2.view.Messages;

public class CherryBomb extends Plant{
	private static final int COST = 50;
	private static final int DAMAGE = 10;
	private static final int ENDURANCE = 2;
	private static final int CYCLES_TO_EXPLODE = 2;
	
	CherryBomb(GameWorld game, int col, int row) {
		super(game, col, row);
	}
	
	public CherryBomb(){
	}

	@Override
	public boolean catchObject() {
		return false;
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
		return Messages.CHERRY_BOMB_NAME;
	}

	@Override
	protected String getNameAndShortcut() {
		return Messages.CHERRY_BOMB_NAME_SHORTCUT;
	}

	@Override
	public String getSymbol() {
		return (contCycles == CYCLES_TO_EXPLODE) ? Messages.CHERRY_BOMB_SYMBOL.toUpperCase():
			Messages.CHERRY_BOMB_SYMBOL;
	}


	@Override
	public void update() {
		if(contCycles == CYCLES_TO_EXPLODE ){
			this.lifePoints = 0;
		}
		
		this.incrementContCycles();
	}


	@Override
	public void onExit() {
		game.addActionToDeque(new ExplosionAction(this.col, this.row, this.getDamage(), true));
	}

	@Override
	public CherryBomb createNew(GameWorld game, int col, int row) {
		return new CherryBomb(game, col, row);
	}

}
