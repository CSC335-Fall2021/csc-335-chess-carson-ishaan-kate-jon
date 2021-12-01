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
}
