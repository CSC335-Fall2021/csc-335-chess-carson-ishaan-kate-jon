package model;

/**
 * This class models out the basic move that a piece can possibly
 * make. This object is created through the various pieces according 
 * to their own respective getPossibleMoves method.
 * 
 * @author carsonchapman
 *
 */
public class Move {
	
	 private int file;
	 private int rank;
	 
	 
	 /**
	  * This is a paramaterized constructor set in place to create the
	  * current possible move. Move takes in 2 integer values for x and y
	  * and assigns the instances of x and y respectively.
	  * 
	  * @param x : pieces x location
	  * @param y : pieces y location
	  */
	 public Move(int y, int x) {
		 this.file = x;
		 this.rank = y;
	 }
	 
	 public int getX() { return file; }
	 public int getY() { return rank; }
}
