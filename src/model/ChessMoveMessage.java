package model;

import java.io.Serializable;

public class ChessMoveMessage implements Serializable {
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	private static final long serialVersionUID = 1L;
	
	private int oldX; 
	private int oldY; 
	
	private int newX; 
	private int newY; 
	
	public ChessMoveMessage(int OldX, int OldY, int NewX, int NewY) {
		this.oldX = OldX;
		this.oldY = OldY;
		this.newX = NewX;
		this.newY = NewY;
	}
	
	public int getOldX() { return oldX; }
	public int getOldY() { return oldY; }
	public int getNewX() { return newX; }
	public int getNewY() { return newY; }
}
