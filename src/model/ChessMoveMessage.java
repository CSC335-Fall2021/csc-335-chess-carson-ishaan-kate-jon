package model;

import java.io.Serializable;
/**
 * This class is the model for the serialized message that gets sent
 * through to the other socket and gets processed within the model.
 * This function includes some methods that the model can call to retrieve
 * its contents.
 * @author carsonchapman
 *
 */
public class ChessMoveMessage implements Serializable {
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	private static final long serialVersionUID = 1L;
	
	private Move oldMove; 
	private Move newMove;
	
	/**
	 * Parameterized constructor, when this runs, a MoveMessage will be 
	 * created and set its values to the values within the parameter of 
	 * the function.
	 * 
	 * @param oldMove
	 * @param newMove
	 */
	public ChessMoveMessage(Move oldMove, Move newMove) {
		this.oldMove = oldMove;
		this.newMove = newMove;
	}
	
	// Getters and Setters
	public Move getoldMove() { return oldMove; }
	public Move getnewMove() { return newMove; }
	public int getOldX() 	 { return oldMove.getX(); }
	public int getOldY() 	 { return oldMove.getY(); } 
	public int getNewX() 	 { return newMove.getX(); }
	public int getNewY() 	 { return newMove.getY(); }
}
