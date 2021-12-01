package model;

public class ChessModel {
	
		private boolean thisTurn = false;
		Piece[][] chessBoard;
		private static final String starterString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		
		public ChessModel() {
			this(starterString);
		}
		
		public ChessModel(String fenString) {
			chessBoard = new Piece[8][8];
			if (fenString == null) {
				createChessBoard(starterString);
			} else {
				createChessBoard(fenString);
			}
		}
		
		public void setMyTurn(boolean mine) {
			thisTurn = mine;
		}
		
		public boolean isMyTurn() {
			return thisTurn;
		}
		
		public Move[] getPossibleMoves(int x, int y) {
			// TODO Auto-generated method stub
			return null;
		}
		
		public void createChessBoard(String fenString) {
			int file = 0;
			int rank = 0;
			for (int i=0; i < fenString.length(); i++) {
				char piece = fenString.charAt(i);
				if (piece == '/') {
					file = 0;
					rank++;
				}
				else if (Character.isAlphabetic(piece)) {
					Piece newPiece = new Piece(piece, file, rank);
					chessBoard[rank][file] = newPiece;
					file++; 
				}
				else if (!Character.isAlphabetic(piece)) { 
					for (int j=0; j < Integer.parseInt("" + piece); j++) {
						Piece newPiece = new Piece(' ', file, rank);
						chessBoard[rank][file] = newPiece;
						file++;
					}
				}
			}
		}
		
		public String getFenString() {
			return getFenString(this.chessBoard);
		}
		
		public String getFenString(Piece[][] curboard) {
			String result = "";
			int count = 0;
			for (int row = 0; row < 8; row++) {
				count = 0;
				for (int col = 0; col < 8; col++) {
					// If slot is space
					if (curboard[row][col].getFenRep() == ' ') {
						count++;
					} else {
						// Else is a piece 
						if (count != 0) {
							result += count;
							count = 0;
						}
						result += curboard[row][col].getFenRep();
					}
					if (count == 8) {
						result += count;
					}
				}
				if (row < 7) {
					result += '/';
				}
			}
			return result;
		}
}
