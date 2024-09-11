package tp1.p2.logic.actions;

import tp1.p2.control.exceptions.GameException;
import tp1.p2.logic.GameWorld;

public interface GameAction {
	void execute(GameWorld game) throws GameException;
}
