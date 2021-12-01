package model;

public class ChessModel {
	
		private boolean thisTurn = false;
		Piece[][] chessBoard;
		
		
		public ChessModel() {
			chessBoard = new Piece[8][8];
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
}
