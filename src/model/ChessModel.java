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
			
			for (int i=0; i < 7; i++) {
				int count = 0;
				for (int j=0; j < 7; j++) {
					if (curboard[j][i].getFenRep() == ' ') {
						count++;
					}
					else {
						if (count != 0) {
							result += count;
						}
						count = 0;
						result += curboard[j][i].getFenRep();
					}
					if (count == 8) {
						result += count;
					}
				}
				result += "/";
			}
			
			return result;
		}
}
