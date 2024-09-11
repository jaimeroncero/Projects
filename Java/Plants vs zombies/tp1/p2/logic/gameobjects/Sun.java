package tp1.p2.logic.gameobjects;

import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

public class Sun extends GameObject {

	// Remember that a Sun is updated the very same cycle is added to the container
	public static final int SUN_COOLDOWN = 10+1;
	
	private static final int SUNCOINS_VALUE = 10;
	
	private static int generatedSuns = 0;
	
	private static int catchedSuns = 0;

	public Sun(GameWorld game, int col, int row) {
		super(game, col, row);
		this.lifePoints = SUN_COOLDOWN;
	}
	
	@Override
	public boolean catchObject() {
		this.lifePoints = 0;
		++Sun.catchedSuns;
		game.increaseSuncoins(SUNCOINS_VALUE);
		return true;
	}

	@Override
	public boolean fillPosition() {
		return false;
	}

	@Override
	public boolean receiveZombieAttack(int damage) {
		return false;
	}

	@Override
	public boolean receivePlantAttack(int damage) {
		return false;
	}

	@Override
	public String getSymbol() {
		return Messages.SUN_SYMBOL;
	}

	@Override
	public String getDescription() {
		return Messages.SUN_DESCRIPTION;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void update() {
		--this.lifePoints;
	}

	@Override
	public void onEnter() {
		Sun.generatedSuns++;
		
	}

	@Override
	public void onExit() {
		
	}

	@Override
	protected int getEndurance() {
		return -1;
	}

	@Override
	protected int getDamage() {
		return -1;
	}

	@Override
	public GameObject createNew(GameWorld game, int col, int row) {
		return null;
	}
	
	public static void resetConts() {
		Sun.catchedSuns = 0;
		Sun.generatedSuns = 0;
	}

	public static int getCatchedSuns() {
		return Sun.catchedSuns;
	}

	public static int getGeneratedSuns() {
		return Sun.generatedSuns;
	}


}
