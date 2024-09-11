package tp1.p2.logic.gameobjects.zombies;

import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;
import tp1.p2.logic.actions.ExplosionAction;

public class ExplosiveZombie extends Zombie{
	
	ExplosiveZombie(GameWorld game, int col, int row){
		super(game, col, row);
	}
	
	public ExplosiveZombie() {
	}

	protected static final int EXPLOSION_DAMAGE = 3;
	
	@Override
	public String getSymbol() {
		return Messages.EXPLOSIVE_ZOMBIE_SYMBOL;
	}
	
	@Override
	public String getName() {
		return Messages.EXPLOSIVE_ZOMBIE_NAME;
	}
	
	
	@Override
	public ExplosiveZombie createNew(GameWorld game, int col, int row) {
		return new ExplosiveZombie(game, col, row);
	}
	
	@Override
	public void onExit() {
		super.onExit();
		ExplosionAction ea = new ExplosionAction(this.col, this.row, EXPLOSION_DAMAGE, false);
		game.addActionToDeque(ea);
	}
	
	@Override
	public boolean receivePlantBombAttack(int damage) {
		this.lifePoints = 0;
		game.incrementScore();
		return true;
	}
}
