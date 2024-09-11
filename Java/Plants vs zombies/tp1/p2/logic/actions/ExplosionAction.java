package tp1.p2.logic.actions;

import tp1.p2.control.exceptions.GameException;
import tp1.p2.logic.GameItem;
import tp1.p2.logic.GameWorld;

public class ExplosionAction implements GameAction {

	private int col;

	private int row;

	private int damage;
	
	private boolean plantIsAttacking;

	public ExplosionAction(int col, int row, int damage, boolean classOfTheAttacker) {
		this.col = col;
		this.row = row;
		this.damage = damage;
		this.plantIsAttacking = classOfTheAttacker;
	}

	@Override
	public void execute(GameWorld game) throws GameException{
		for(int i = this.col - 1; i <= this.col + 1; i++) {
			for(int j = this.row - 1; j <= this.row + 1; j++) {
				if(game.isValidPosition(i, j)) {
					
					GameItem g = game.getFillingObjectInPosition(i, j);
					
					if(g != null) {
						
						if(plantIsAttacking) {
							g.receivePlantBombAttack(damage);
							
						}
								
						else {
							g.receiveZombieAttack(damage);
						}
					}				
				}
			}
		}
	}

}
