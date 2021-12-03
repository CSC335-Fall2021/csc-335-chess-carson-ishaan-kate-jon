package model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

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
		Piece curPiece = chessBoard[x][y];
		if (curPiece.getType() == 'p' || curPiece.getType() == 'P') {
			// TODO implement pawn logic
		} else if (curPiece.getType() == 'r' || curPiece.getType() == 'R') {
			// TODO implement rook logic
		} else if (curPiece.getType() == 'n' || curPiece.getType() == 'N') {
			// TODO implement knight logic
		} else if (curPiece.getType() == 'b' || curPiece.getType() == 'B') {
			// TODO implement bishop logic
		} else if (curPiece.getType() == 'q' || curPiece.getType() == 'Q') {
			// TODO implement queen logic
		} else if (curPiece.getType() == 'k' || curPiece.getType() == 'K') {
			// TODO implement king logic
		}
		return null;
	}

	public void makeMove(Move oldMove, Move newMove) {
		Piece emptyReplacement = new Piece(' ', oldMove.getX(), oldMove.getY());
		chessBoard[newMove.getX()][newMove.getY()] = chessBoard[oldMove.getX()][oldMove.getY()];
		chessBoard[oldMove.getX()][oldMove.getY()] = emptyReplacement;
	}

	public void createChessBoard(String fenString) {
		int file = 0;
		int rank = 0;
		for (int i = 0; i < fenString.length(); i++) {
			char piece = fenString.charAt(i);
			if (piece == '/') {
				file = 0;
				rank++;
			} else if (Character.isAlphabetic(piece)) {
				Piece newPiece = new Piece(piece, file, rank);
				chessBoard[rank][file] = newPiece;
				file++;
			} else if (!Character.isAlphabetic(piece)) {
				for (int j = 0; j < Integer.parseInt("" + piece); j++) {
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
				if (count != 0 && col == 7) {
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
