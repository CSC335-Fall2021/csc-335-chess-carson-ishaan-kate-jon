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
	
		private char fenRep;
		private int color;
		
		private int type;
		
		private int file;
		private int rank;
		
		public Piece(char fenChar, int file, int rank) {
			this.file = file;
			this.rank = rank;
			this.fenRep = fenChar;
			
		}
		
		char getFenRep() {
			return fenRep;
		}
		
		int getColor() {
			return color;
		}
		
		void setColor(int color) {
			this.color = color;
		}
		
		Move[] getPossibleMoves(int x, int y, Object[][] chessBoard) {
			return new Move[5];
		}
		
}
