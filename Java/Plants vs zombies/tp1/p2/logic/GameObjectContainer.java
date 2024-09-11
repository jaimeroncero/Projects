package tp1.p2.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.p2.control.exceptions.GameException;
import tp1.p2.control.exceptions.NotCatchablePositionException;
import tp1.p2.logic.gameobjects.GameObject;
import tp1.p2.view.Messages;

public class GameObjectContainer {

	private List<GameObject> gameObjects;

	public GameObjectContainer() {
		gameObjects = new ArrayList<>();
	}

	public String positionToString(int col, int row) {
		StringBuilder buffer = new StringBuilder();
		boolean sunPainted = false;
		boolean sunAboutToPaint = false;

		for (GameObject g : gameObjects) {
			if(g.isAlive() && g.getCol() == col && g.getRow() == row) {
				String objectText = g.toString();
				sunAboutToPaint = objectText.indexOf(Messages.SUN_SYMBOL) >= 0;
				if (sunAboutToPaint) {
					if (!sunPainted) {
						buffer.append(objectText);
						sunPainted = true;
					}
				} else {
					buffer.append(objectText);
				}
			}
		}

		return buffer.toString();
	}

	public boolean removeDead() {
		for(int i = 0; i < gameObjects.size(); ++i)
			if(!gameObjects.get(i).isAlive()) {
				gameObjects.get(i).onExit();
				gameObjects.remove(i);
				return true;
			}
		
		return false;
	}

	public void clear() {
		gameObjects.clear();
		
	}


	public GameObject getObjectInPosition(int col, int row) {
		for(GameObject g : gameObjects) {
			if(g.isInPosition(col, row)) return g;
		}
		
		return null;
		
	}
	
	public GameObject getFillingObjectInPosition(int col, int row) {
		for(GameObject g : gameObjects) {
			if(g.isInPosition(col, row) && g.fillPosition()) return g;
		}
		
		return null;
		
	}

		/**
		
		 * @param col
		 * @param row
		 * @return true if there are any game object in (col, row).
		 * (!) If position is only occupied by {@link Sun}, return is also true.
		 */
	public boolean isEmpty(int col, int row) {
		GameObject g = getObjectInPosition(col, row);
		return  g == null;
	}

	public void addGameObject(GameObject g) {
		gameObjects.add(g);		
		g.onEnter();
	}
	
	public void addZombie(GameObject z) {
		gameObjects.add(z);
	}

	public void update() {
		// Can't use for-each loop (for(GameObject g : gameObjexts)) without errors.
		for(int i = 0; i < gameObjects.size(); i++) {
			GameObject g = gameObjects.get(i);
			if(g.isAlive()) {
				g.update();
			}
		}
	}
	
	
	public boolean isFullyOccupied(int col, int row) {
		int i=0;
		boolean fullyOcuppied = false;

		while (i<gameObjects.size() && !fullyOcuppied) {
			GameObject g = gameObjects.get(i);
			if (g.isAlive() && g.isInPosition(col, row)) {
				fullyOcuppied = g.fillPosition();
			}
			i++;
		}

		return fullyOcuppied;
	}

	public boolean checkZombiesWin() {
		for(GameObject g : gameObjects) {
			if(g.getCol() == -1) return true;
			//una planta no deberia poder estar en la col -1
		}
		
		return false;
	}

	/**
	 * Catches all game objects that shared the position (col, row)
	 * @param col
	 * @param row
	 * @return true if some {@link Sun} has been catch. False otherwise.
	 * @throws NotCatchablePositionException no sun
	 */
	public void tryToCatchObjectInPosition(int col, int row) throws GameException{
		boolean someSunCatch = false;
		
		for(GameObject g : gameObjects) {
			if(g.isInPosition(col, row)) {
				boolean catchthis = g.catchObject();
				someSunCatch = someSunCatch || catchthis;
			}
		}
		if(!someSunCatch) throw new NotCatchablePositionException(Messages.NO_CATCHABLE_IN_POSITION);
	}
}
