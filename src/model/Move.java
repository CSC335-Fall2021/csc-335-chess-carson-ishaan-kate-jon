package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class models out the basic move that a piece can possibly
 * make. This object is created through the various pieces according 
 * to their own respective getPossibleMoves method.
 * 
 * @author carsonchapman
 *
 */
public class Move implements Serializable{
	
	 private int file;
	 private int rank;
	 private static final long serialVersionUID = 1L;

	 
	 /**
	  * This is a paramaterized constructor set in place to create the
	  * current possible move. Move takes in 2 integer values for x and y
	  * and assigns the instances of x and y respectively.
	  * 
	  * @param x : pieces x location
	  * @param y : pieces y location
	  */
	 public Move(int x, int y) {
		 this.file = x;
		 this.rank = y;
	 }
	 
	 public int getX() { return file; }
	 public int getY() { return rank; }
	 
	 @Override
	 public boolean equals(Object obj) {
	    if(this == obj)
	            return true;
	        if(obj == null || obj.getClass() != this.getClass()) {
	            return false;
	        } 
	        Move newMove = (Move) obj;
	        return (newMove.getX() == this.getX() && newMove.getY() == this.getY());
	 }
	 
	@Override
    public int hashCode() {
		ArrayList<Integer> retArr = new ArrayList<Integer>();
		retArr.add(this.getX());
		retArr.add(this.getY());
        return retArr.hashCode();
    }
}
