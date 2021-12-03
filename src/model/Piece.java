package model;

/*
 * This interface models out the base type for each piece. This interface
 * includes a method which returns an array of possible moves for the piece.
 * The program's view will then retrieve this array and restrict the user 
 * from clicking anywhere except for the spaces according to the pieces available
 * moves.
 * 
 */
public class Piece {
	
		private int color;
		
		private char type;
		
		private int file;
		private int rank;
		
		public Piece(char fenChar, int file, int rank) {
			this.file = file;
			this.rank = rank;
			this.type = fenChar;
			
			// upper case = white, lower = black
			if (Character.isUpperCase(fenChar)) {
				this.color = 1;
			} else {
				this.color = 2;
			}
			
		}
		
		char getFenRep() {
			return type;
		}
		
		int getColor() {
			return color;
		}
		
		Move[] getPossibleMoves(int x, int y, Object[][] chessBoard) { 
			return new Move[5];
		}

		public char getType() {
			return type;
		}


		public int getFile() {
			return file;
		}
	

		public int getRank() {
			return rank;
		}

		public void setX(int x) {
			this.file = x;
			
		}

		public void setY(int y) {
			this.rank = y;
			
		}

		
		
}
