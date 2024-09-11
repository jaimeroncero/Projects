package tp1.p2.logic;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import tp1.p2.control.Command;
import tp1.p2.control.Level;
import tp1.p2.control.exceptions.*;
import tp1.p2.logic.actions.GameAction;
import tp1.p2.logic.gameobjects.GameObject;
import tp1.p2.logic.gameobjects.plants.*;
import tp1.p2.logic.gameobjects.zombies.*;
import tp1.p2.view.Messages;

public class Game implements GameStatus, GameWorld {
	
	private static final int STARTING_SUNCOINS = 50;

	private long seed;

	private Level level;
	
	private int cycle;

	private GameObjectContainer container;

	private Deque<GameAction> actions;
	
	private ZombiesManager zombiesManager;

	private boolean playerQuits;
	
	private int suncoins;
	
	private boolean plantsWin;
	
	private boolean zombiesWin;
	
	private SunsManager sunsManager;
	
	private int score;
	
	private boolean isThereANewRecord;
	
	private Record record;
	

	public Game(long seed, Level level) throws GameException, IOException {
		this.seed = seed;
		this.level = level;
		this.container = new GameObjectContainer();
		reset();
	}

	/**
	 * Resets the game.
	 * @throws IOException 
	 */
	@Override
	public void reset() throws GameException, IOException {
		reset(this.level, this.seed);
	}
	


	/**
	 * Resets the game with the provided level and seed.
	 * 
	 * @param level {@link Level} Used to initialize the game.
	 * @param seed Random seed Used to initialize the game.
	 * @throws IOException 
	 */

	
	@Override
	public void reset(Level level, long seed) throws GameException, IOException {
		Random random = new Random(seed);
		this.playerQuits = false;
		this.suncoins = STARTING_SUNCOINS;
		this.zombiesManager = new ZombiesManager(this, level , random);
		this.container.clear();
		this.seed = seed;
		this.level = level;
		this.cycle = 0;
		this.actions = new ArrayDeque<>();
		this.plantsWin = false;
		this.zombiesWin = false;
		this.sunsManager = new SunsManager(this, random);
		this.score = 0;
		this.record = Record.loadFile(level);
		printStart();
	}
	
	public void printStart() {
		StringBuilder buffer = new StringBuilder(Messages.CONFIGURED_LEVEL.formatted(level))
		.append(Messages.LINE_SEPARATOR)
		.append(Messages.CONFIGURED_SEED.formatted(seed))
		.append(Messages.LINE_SEPARATOR);
		
		System.out.print(buffer);
	}


	/**
	 * Executes the game actions and update the game objects in the board.
	 * 
	 */
	@Override
	public void update() throws GameException {

		// 1. Execute pending actions
		executePendingActions();

		// 2. Execute game Actions
		zombiesManager.update();
		sunsManager.update();

		// 3. Game object updates
		container.update();

		// 4. & 5. Remove dead and execute pending actions
		boolean deadRemoved = true;
		while (deadRemoved || areTherePendingActions()) {
			// 4. Remove dead
			deadRemoved = this.container.removeDead();

			// 5. execute pending actions
			executePendingActions();
		}

		this.cycle++;
		checkPlantsWin();
		checkZombiesWin();

		// 6. Notify commands that a new cycle started
		Command.newCycle();
		
		
		// 7. Update record
		
		this.isThereANewRecord = this.record.update(this.score);		
	}
	
	public boolean getIsThereANewRecord() {
		return this.isThereANewRecord;
	}
	
	public void setNewRecord() throws GameException, IOException {
		this.record.save();
	}
	

	private void executePendingActions() throws GameException {
		while (!this.actions.isEmpty()) {
			GameAction action = this.actions.removeLast();
			action.execute(this);
		}
	}

	private boolean areTherePendingActions() {
		return this.actions.size() > 0;
	}
	
	@Override
	public int getCycle() { return this.cycle; }
	
	@Override
	public int getSuncoins() { return this.suncoins; }
	
	@Override
	public int getRemainingZombies() { return this.zombiesManager.getRemainingZombies(); }

	
	@Override 
	public void peashooterAttack(int peasCol, int peasRow, int damage){
		
		for(int col = peasCol + 1; col < NUM_COLS; ++col) {
			GameItem target = container.getFillingObjectInPosition(col, peasRow);
			if(target != null && target.receivePlantAttack(damage)) break;
		}
	}
	
	@Override
	public GameItem getObjectInPosition(int col, int row) {
		return container.getObjectInPosition(col, row);
	}
	
	@Override
	public boolean isEmpty(int col, int row) {
		return container.isEmpty(col,row);
	}


	@Override
	public void increaseSuncoins(int suncoinsProduced) {
		this.suncoins += suncoinsProduced;
		
	}

	@Override
	public String positionToString(int col, int row) {
		return container.positionToString(col, row);
	}


	@Override
	public void addGameObject(GameObject g) {
		container.addGameObject(g);

	}

	public boolean execute(Command command) throws GameException, IOException{
		return command.execute(this);
	}
	
	@Override
	public boolean isPlayerQuits() {
		return playerQuits;
	}
	
	@Override
	public void setPlayerQuits(boolean b) {
		playerQuits = b;
	}
	
	@Override
	public boolean isFinished() {
		return plantsWin || zombiesWin;
	}
	
	@Override
	public boolean getPlantsWin() {
		return plantsWin;
	}
	
	@Override
	public boolean getZombiesWin() {
		return zombiesWin;
	}

	
	@Override
	public boolean isValidPosition(int col, int row){
		return col >= 0 && col < NUM_COLS && row >= 0 && row < NUM_ROWS;
	}
	
	@Override
	public boolean isValidPositionForAZombie(int col, int row) throws GameException{
		if(col >= 0 && col <= NUM_COLS && row >= 0 && row < NUM_ROWS) return true;
		else {
			throw new InvalidPositionException(Messages.INVALID_POSITION.formatted(col, row));
		}
	}
	
	

	@Override
	public boolean addPlant(Plant p) {
		if(this.suncoins >= p.getCost()) {
			addGameObject(p);
			suncoins -= p.getCost();
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Checks if a cell is fully occupied, that is, the position can be shared between an NPC (Plant, Zombie) and Suns .
	 * 
	 * @param col Column of the cell
	 * @param row Row of the cell
	 * 
	 * @return <code>true</code> if the cell is fully occupied, <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean isFullyOcuppied(int col, int row) {
		return this.container.isFullyOccupied(col, row);
	} 

	private void checkPlantsWin() {
		if(zombiesManager.checkPlantsWin())
			this.plantsWin = true;
	}

	@Override
	public void decrementZombiesAlived() {
		zombiesManager.decrementZombiesAlived();
		
	}
	
	private void checkZombiesWin() {
		if(container.checkZombiesWin())
			this.zombiesWin = true;
	}

	@Override
	public void addZombie(Zombie zombie) {
		container.addZombie(zombie); //no invoca a onEnter()
		zombiesManager.incrementZombiesAlived();
		
	}

	@Override
	public void addActionToDeque(GameAction ea) {
		this.actions.add(ea);	
	}

	@Override
	public void incrementZombiesAlived() {
		this.zombiesManager.incrementZombiesAlived();
		
	}

	@Override
	public void decrementRemainingZombies() {
		this.zombiesManager.decrementRemainingZombies();
		
	}

	@Override
	public int getGeneratedSuns() {
		return sunsManager.getGeneratedSuns();
	}

	@Override
	public int getCaughtSuns() {
		return sunsManager.getCatchedSuns();
	}

	@Override
	public void tryToCatchObjectInPosition(int col, int row) throws GameException {
		if(!isEmpty(col, row)) container.tryToCatchObjectInPosition(col, row);
		else throw new NotCatchablePositionException(Messages.NO_CATCHABLE_IN_POSITION.formatted(col, row));
	}

	@Override
	public void addSun(int suncoins) {
		sunsManager.addSun();
	}

	@Override
	public GameItem getFillingObjectInPosition(int col, int row) {
		return container.getFillingObjectInPosition(col, row);
	}

	@Override
	public void incrementScore() {
		this.score += 10;		
	}

	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	public Level getLevel() {
		return this.level;
	}

	@Override
	public Record getRecord() {
		return this.record;
	}



	
	

}
