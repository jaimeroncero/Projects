package tp1.p2.logic.gameobjects.zombies;

import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class BucketHead extends Zombie{
	
	protected static final int SPEED = 4; 
	protected static final int ENDURANCE = 8;

	public BucketHead(){
		
	}
	
	BucketHead(GameWorld game, int col, int row) {
		super(game, col, row);
	}

	@Override
	public String getSymbol() {
		return Messages.BUCKET_HEAD_ZOMBIE_SYMBOL;
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
		return Messages.BUCKET_HEAD_ZOMBIE_NAME;
	}
	
	@Override
	public BucketHead createNew(GameWorld game, int col, int row) {
		return new BucketHead(game, col, row);
	}
}
