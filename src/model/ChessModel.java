package model;

import java.util.ArrayList;

import javafx.beans.InvalidationListener;
import java.util.Observable;

public class ChessModel extends Observable {

	private boolean thisTurn = false;
	Piece[][] chessBoard;
	private final int rows;
	private final int cols;
	private static final String starterString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

	public ChessModel() {
		this(starterString);
	}

	public ChessModel(String fenString) {
		chessBoard = new Piece[8][8];
		this.rows = 8;
		this.cols = 8;
		if (fenString == null) {
			createChessBoard(starterString);
		} else {
			createChessBoard(fenString);
		}
	}
	
	public ChessModel(String fenString, int rows, int cols) {
		chessBoard = new Piece[rows][cols];
		this.rows = rows;
		this.cols = cols;
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

	public ArrayList<Move> getPossibleMoves(int file, int rank) {
		Piece curPiece = chessBoard[rank][file];
		char curType = curPiece.getType();
		if (curType == 'p' || curType == 'P') {
			return getMovesPawn(curPiece);
		} else if (curType == 'r' || curType == 'R') {
			// TODO implement rook logic
			return getMovesRook(curPiece);
		} else if (curType == 'n' || curType == 'N') {
			// TODO implement knight logic
			return getMovesKnight(curPiece);
		} else if (curType == 'b' || curType == 'B') {
			// TODO implement bishop logic
			return getMovesBishop(curPiece);
		} else if (curType == 'q' || curType == 'Q') {
			// TODO implement queen logic
			return getMovesQueen(curPiece);
		} else if (curType == 'k' || curType == 'K') {
			// TODO implement king logic
			return getMovesKing(curPiece);
		}
		return null;
	}

	private ArrayList<Move> getMovesPawn(Piece curPiece) {
		ArrayList<Move> retArr = new ArrayList<Move>();
		// If piece is black
		if (curPiece.getType() == 'p') {
			// If pawn is in start position, can move 2
			if (curPiece.getRank() == 1) {
				if (chessBoard[curPiece.getRank()+2][curPiece.getFile()].getType() == ' ') {
					retArr.add(new Move(curPiece.getFile(), curPiece.getRank()+2));
				}
			}
			// If move forward 1 is empty space
			if (chessBoard[curPiece.getRank()+1][curPiece.getFile()].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile(), curPiece.getRank()+1));
			}
			// If move 1 diagonal right is an opposing piece
			if (0 <= curPiece.getFile()+1 && curPiece.getFile()+1 < this.cols &&
					Character.isAlphabetic(chessBoard[curPiece.getRank()+1][curPiece.getFile()+1].getType()) &&
			    chessBoard[curPiece.getRank()+1][curPiece.getFile()+1].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()+1, curPiece.getRank()+1));
			}
			// If move 1 diagonal left is an opposing piece
			if (0 <= curPiece.getFile()-1 && curPiece.getFile()-1 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()+1][curPiece.getFile()-1].getType()) &&
				chessBoard[curPiece.getRank()+1][curPiece.getFile()-1].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()-1, curPiece.getRank()+1));
			}
		} else if (curPiece.getType() == 'P') {
			// Else if piece is white
			// If pawn is in start position, can move 2
			if (curPiece.getRank() == 6) {
				if (chessBoard[curPiece.getRank()-2][curPiece.getFile()].getType() == ' ') {
					retArr.add(new Move(curPiece.getFile(), curPiece.getRank()-2));
				}
			}
			// If move forward 1 is empty space
			if (chessBoard[curPiece.getRank()-1][curPiece.getFile()].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile(), curPiece.getRank()-1));
			}
			// If move 1 diagonal right is an opposing piece
			if (0 <= curPiece.getFile()+1 && curPiece.getFile()+1 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()-1][curPiece.getFile()+1].getType()) &&
				chessBoard[curPiece.getRank()-1][curPiece.getFile()+1].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()+1, curPiece.getRank()-1));
			}
			// If move 1 diagonal left is an opposing piece
			if (0 <= curPiece.getFile()-1 && curPiece.getFile()-1 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()-1][curPiece.getFile()-1].getType()) &&
				chessBoard[curPiece.getRank()-1][curPiece.getFile()-1].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()-1, curPiece.getRank()-1));
			}
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	/*
	 * Compared to other chess pieces, the knight's movement is unique: it may move two 
	 * squares vertically and one square horizontally, or two squares horizontally and 
	 * one square vertically (with both forming the shape of an L). This way, a knight 
	 * can have a maximum of 8 moves. While moving, the knight can jump over pieces to 
	 * reach its destination Knights capture in the same way, replacing the enemy piece 
	 * on the square and removing it from the board. Knights and pawns are 
	 * the only pieces that can be moved in the initial position.
	 */
	private ArrayList<Move> getMovesKnight(Piece curPiece) {
		ArrayList<Move> retArr = new ArrayList<Move>();
		// ------------------------ Check the new spot is empty -------------------------
		// Check out of bounds on move forward 2
		if (0 <= curPiece.getRank()+2 && curPiece.getRank()+2 < this.rows) {
			// Move 2 forward and 1 left
			if (0 <= curPiece.getFile()-1 && curPiece.getFile()-1 < this.cols && 
					chessBoard[curPiece.getRank()+2][curPiece.getFile()-1].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile()-1, curPiece.getRank()+2));
			}
			// Move 2 forward and 1 right
			if (0 <= curPiece.getFile()+1 && curPiece.getFile()+1 < this.cols &&
					chessBoard[curPiece.getRank()+2][curPiece.getFile()+1].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile()+1, curPiece.getRank()+2));
			}
		}
		// Check out of bounds on move backward 2
		if (0 <= curPiece.getRank()-2 && curPiece.getRank()-2 < this.rows) {
			// Move 2 backward and 1 left
			if (0 <= curPiece.getFile()-1 && curPiece.getFile()-1 < this.cols &&
					chessBoard[curPiece.getRank()-2][curPiece.getFile()-1].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile()-1, curPiece.getRank()-2));
			}
			// Move 2 backward and 1 right
			if (0 <= curPiece.getFile()+1 && curPiece.getFile()+1 < this.cols &&
					chessBoard[curPiece.getRank()-2][curPiece.getFile()+1].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile()+1, curPiece.getRank()-2));
			}
		}
		// Check out of bounds on move forward 1
		if (0 <= curPiece.getRank()+1 && curPiece.getRank()+1 < this.rows) {
			// Move 1 forward and 2 left
			if (0 <= curPiece.getFile()-2 && curPiece.getFile()-2 < this.cols &&
					chessBoard[curPiece.getRank()+1][curPiece.getFile()-2].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile()-2, curPiece.getRank()+1));
			}
			// Move 1 forward and 2 right
			if (0 <= curPiece.getFile()+2 && curPiece.getFile()+2 < this.cols &&
					chessBoard[curPiece.getRank()+1][curPiece.getFile()+2].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile()+2, curPiece.getRank()+1));
			}
		}
		// Check out of bounds on move backward 1
		if (0 <= curPiece.getRank()-1 && curPiece.getRank()-1 < this.rows) {
			// Move 1 backward and 2 left
			if (0 <= curPiece.getFile()-2 && curPiece.getFile()-2 < this.cols &&
					chessBoard[curPiece.getRank()-1][curPiece.getFile()-2].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile()-2, curPiece.getRank()-1));
			}
			// Move 1 backward and 2 right
			if (0 <= curPiece.getFile()+2 && curPiece.getFile()+2 < this.cols &&
					chessBoard[curPiece.getRank()-1][curPiece.getFile()+2].getType() == ' ') {
				retArr.add(new Move(curPiece.getFile()+2, curPiece.getRank()-1));
			}
		}
		
		// ---------------- Check the new spot has an opponent piece -----------------
		// Check out of bounds on move forward 2
		if (0 <= curPiece.getRank()+2 && curPiece.getRank()+2 < this.rows) {
			// Move 2 forward and 1 left
			if (0 <= curPiece.getFile()-1 && curPiece.getFile()-1 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()+2][curPiece.getFile()-1].getType()) &&
				chessBoard[curPiece.getRank()+2][curPiece.getFile()-1].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()-1, curPiece.getRank()+2));
			}
			// Move 2 forward and 1 right
			if (0 <= curPiece.getFile()+1 && curPiece.getFile()+1 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()+2][curPiece.getFile()+1].getType()) &&
					chessBoard[curPiece.getRank()+2][curPiece.getFile()+1].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()+1, curPiece.getRank()+2));
			}
		}
		// Check out of bounds on move backward 2
		if (0 <= curPiece.getRank()-2 && curPiece.getRank()-2 < this.rows) {
			// Move 2 backward and 1 left
			if (0 <= curPiece.getFile()-1 && curPiece.getFile()-1 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()-2][curPiece.getFile()-1].getType()) &&
					chessBoard[curPiece.getRank()-2][curPiece.getFile()-1].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()-1, curPiece.getRank()-2));
			}
			// Move 2 backward and 1 right
			if (0 <= curPiece.getFile()+1 && curPiece.getFile()+1 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()-2][curPiece.getFile()+1].getType()) &&
					chessBoard[curPiece.getRank()-2][curPiece.getFile()+1].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()+1, curPiece.getRank()-2));
			}
		}
		// Check out of bounds on move forward 1
		if (0 <= curPiece.getRank()+1 && curPiece.getRank()+1 < this.rows) {
			// Move 1 forward and 2 left
			if (0 <= curPiece.getFile()-2 && curPiece.getFile()-2 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()+1][curPiece.getFile()-2].getType()) &&
					chessBoard[curPiece.getRank()+1][curPiece.getFile()-2].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()-2, curPiece.getRank()+1));
			}
			// Move 1 forward and 2 right
			if (0 <= curPiece.getFile()+2 && curPiece.getFile()+2 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()+1][curPiece.getFile()+2].getType()) &&
					chessBoard[curPiece.getRank()+1][curPiece.getFile()+2].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()+2, curPiece.getRank()+1));
			}
		}
		// Check out of bounds on move backward 1
		if (0 <= curPiece.getRank()-1 && curPiece.getRank()-1 < this.rows) {
			// Move 1 backward and 2 left
			if (0 <= curPiece.getFile()-2 && curPiece.getFile()-2 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()-1][curPiece.getFile()-2].getType()) &&
					chessBoard[curPiece.getRank()-1][curPiece.getFile()-2].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()-2, curPiece.getRank()-1));
			}
			// Move 1 backward and 2 right
			if (0 <= curPiece.getFile()+2 && curPiece.getFile()+2 < this.cols && 
					Character.isAlphabetic(chessBoard[curPiece.getRank()-1][curPiece.getFile()+2].getType()) &&
					chessBoard[curPiece.getRank()-1][curPiece.getFile()+2].getColor() != curPiece.getColor()) {
				retArr.add(new Move(curPiece.getFile()+2, curPiece.getRank()-1));
			}
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	private ArrayList<Move> getMovesRook(Piece curPiece) {
		ArrayList<Move> retArr = new ArrayList<Move>();
		if (curPiece.getType() == 'r') {
			;
		} else if (curPiece.getType() == 'R') {
			;
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	private ArrayList<Move> getMovesBishop(Piece curPiece) {
		ArrayList<Move> retArr = new ArrayList<Move>();
		if (curPiece.getType() == 'b') {
			;
		} else if (curPiece.getType() == 'B') {
			;
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	/*
	 * The queen (♕, ♛) is the most powerful piece in the game of chess, able to move any
	 * number of squares vertically, horizontally or diagonally, unless the square is already
	 * occupied by a friendly piece
	 */
	private ArrayList<Move> getMovesQueen(Piece curPiece) {
		ArrayList<Move> retArr = new ArrayList<Move>();
		if (curPiece.getType() == 'q') {
			;
		} else if (curPiece.getType() == 'Q') {
			;
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	/* A king can move one square in any direction (horizontally, vertically, or diagonally),
	   unless the square is already occupied by a friendly piece, or the move would place the
	   king in check. The king is also involved in the special move of castling. */
	private ArrayList<Move> getMovesKing(Piece curPiece) {
		ArrayList<Move> retArr = new ArrayList<Move>();
		if (curPiece.getType() == 'k') {
			;
		} else if (curPiece.getType() == 'K') {
			;
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}

	public void makeMove(Move oldMove, Move newMove) {
		Piece emptyReplacement = new Piece(' ', oldMove.getX(), oldMove.getY());
		chessBoard[newMove.getY()][newMove.getX()] = chessBoard[oldMove.getY()][oldMove.getX()];
		chessBoard[newMove.getY()][newMove.getX()].setX(newMove.getX());
		chessBoard[newMove.getY()][newMove.getX()].setY(newMove.getY());
		chessBoard[oldMove.getY()][oldMove.getX()] = emptyReplacement;
		
		setChanged();
		notifyObservers(getFenString());
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
