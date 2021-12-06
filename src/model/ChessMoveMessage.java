package model;

import java.io.Serializable;

public class ChessMoveMessage implements Serializable {
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	private static final long serialVersionUID = 1L;
	
	private Move oldMove; 
	private Move newMove;
	
	public ChessMoveMessage(Move oldMove, Move newMove) {
		this.oldMove = oldMove;
		this.newMove = newMove;
	}
	
	public Move getoldMove() { return oldMove; }
	public Move getnewMove() { return newMove; }
	public int getOldX() 	 { return oldMove.getX(); }
	public int getOldY() 	 { return oldMove.getY(); } 
	public int getNewX() 	 { return newMove.getX(); }
	public int getNewY() 	 { return newMove.getY(); }
}
