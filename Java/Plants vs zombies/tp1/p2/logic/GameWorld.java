package tp1.p2.logic;

import java.io.IOException;

import tp1.p2.control.Level;
import tp1.p2.control.exceptions.GameException;
import tp1.p2.logic.actions.GameAction;
import tp1.p2.logic.gameobjects.GameObject;
import tp1.p2.logic.gameobjects.Sun;
import tp1.p2.logic.gameobjects.plants.Plant;
import tp1.p2.logic.gameobjects.zombies.Zombie;

public interface GameWorld {

	public static final int NUM_ROWS = 4;

	public static final int NUM_COLS = 8;


	/**
	 * Resets the game with the provided level and seed.
	 * 
	 * @param level {@link Level} Used to initialize the game.
	 * @param seed Random seed Used to initialize the game.
	 * @throws IOException 
	 */
	void reset(Level level, long seed) throws GameException, IOException;

	/**
	 * Resets the game.
	 * @throws IOException 
	 */
	void reset() throws GameException, IOException;

	/**
	 * Executes the game actions and update the game objects in the board.
	 * @throws IOException 
	 * 
	 */
	void update() throws GameException;

	void peashooterAttack(int peasCol, int peasRow, int damage);

	void increaseSuncoins(int suncoinsProduced);

	/**
	 * 
	 * @param col
	 * @param row
	 * @return the first game object found in (col, row) - including suns.
	 */
	GameItem getObjectInPosition(int col, int row);

	/**
	
	 * @param col
	 * @param row
	 * @return true if there are any game object in (col, row).
	 * (!) If position is only occupied by {@link Sun}, return is also true.
	 */
	boolean isEmpty(int col, int row);

	void tryToCatchObjectInPosition(int col, int row) throws GameException;

	void setPlayerQuits(boolean b);
	
	/**
	 * 
	 * @param col
	 * @param row
	 * @return true if the position (col, row) is between the limits. False otherwise.
	 * @throws GameException 
	 */
	boolean isValidPosition(int col, int row) throws GameException;

	boolean addPlant(Plant p);

	void decrementZombiesAlived();

	void addZombie(Zombie zombie);

	void addGameObject(GameObject g);

	void addActionToDeque(GameAction ea);
	
	void addSun(int suncoins);

	void incrementZombiesAlived();

	void decrementRemainingZombies();


	/**
	 * Checks if a cell is fully occupied, that is, the position can be shared between an NPC (Plant, Zombie) and Suns .
	 * 
	 * @param col Column of the cell
	 * @param row Row of the cell
	 * 
	 * @return <code>true</code> if the cell is fully occupied, <code>false</code>
	 *         otherwise.
	 */
	boolean isFullyOcuppied(int col, int row);

	GameItem getFillingObjectInPosition(int col, int row);

	boolean isValidPositionForAZombie(int col, int row) throws GameException;

	void incrementScore();

	Level getLevel();

	Record getRecord();


}
