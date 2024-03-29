package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javafx.beans.InvalidationListener;
import java.util.Observable;
import java.util.Set;

public class ChessModel extends Observable {
	
	private boolean isGameOver;
	public ArrayList<String> puzzles;
	private boolean thisTurn = false;
	public Piece[][] chessBoard;
	public ArrayList<Piece> whitePieces;
	public ArrayList<Piece> blackPieces;
	private final int rows;
	private final int cols;
	private static final String starterString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

	public ChessModel() {
		this(starterString, 8, 8);
		this.isGameOver = false;
	}

	public ChessModel(String fenString) {
		this(fenString, 8, 8);
	}
	
	public ChessModel(String fenString, int rows, int cols) {
		chessBoard = new Piece[rows][cols];
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		this.rows = rows;
		this.cols = cols;
		if (fenString == null) {
			createChessBoard(starterString);
		} else {
			createChessBoard(fenString);
		}
	}
	
	// 6 preset puzzles with one move to Checkmate. Preset puzzles in fenstring form.
	// For future implementation of puzzles for player to choose a puzzle number to correspond to index
	// of puzzles list containing a fenstring of a puzzle.
	public ArrayList<String> puzzles() {
		ArrayList<String> puzzleStrings = new ArrayList<String>();
		puzzleStrings.add("8/p1k2pb1/r3p1p1/3n2Np/1B5P/PP1P1P2/K5P1/1Q6"); // e5 -> g4 to checkmate
		puzzleStrings.add("k7/1Rp5/p1P1p3/5b2/8/1PQ5/1KP4q/8"); // f3 -> f5 to checkmate
		puzzleStrings.add("1kr2b1r/1pp3pp/p1qpbn2/1N2pp2/P3P3/3P1P2/1PP2QPP/R1BK2NR"); // c2 -> h7 to checkmate
		puzzleStrings.add("1kr4b/pppb4/2nq3p/8/5N2/1Q1BrP2/P1P3PP/1R1R1K2"); //g2 -> g7 to checkmate
		puzzleStrings.add("1k6/ppp5/q7/7Q/2N5/1P6/P3Np2/b2K4"); // a5 -> d8 to checkmate
		puzzleStrings.add("3R2r1/8/p1k2p2/1p5p/4n1pP/qP6/2P3PQ/1K1R4"); // d4 -> f3 to checkmate
		
		
		return puzzleStrings;
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
				if (chessBoard[curPiece.getRank()+1][curPiece.getFile()].getType() == ' ' &&
						chessBoard[curPiece.getRank()+2][curPiece.getFile()].getType() == ' ') {
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
				if (chessBoard[curPiece.getRank()-1][curPiece.getFile()].getType() == ' ' &&
						chessBoard[curPiece.getRank()-2][curPiece.getFile()].getType() == ' ') {
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
	
	// ------------------------- Start of new untested movement logic ---------------------------------
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
		int pieceRow = curPiece.getRank();
		int pieceCol = curPiece.getFile();
		// Loop for forward moving
		for (int i = pieceRow - 1; i >= 0; i--) {
			// If the new row index has a piece that is not empty
			if (Character.isAlphabetic(chessBoard[i][pieceCol].getType())) {
				// Check for if blocking piece is opposing piece, add to list if yes
				if (chessBoard[i][pieceCol].getColor() != curPiece.getColor()) {
					retArr.add(new Move(pieceCol, i));
				}
				break;
			} else {
				retArr.add(new Move(pieceCol, i));
			}
		}
		// Loop for backward moving
		for (int i = pieceRow + 1; i < this.rows; i++) {
			// If the new row index has a piece that is not empty
			if (Character.isAlphabetic(chessBoard[i][pieceCol].getType())) {
				// Check for if blocking piece is opposing piece, add to list if yes
				if (chessBoard[i][pieceCol].getColor() != curPiece.getColor()) {
					retArr.add(new Move(pieceCol, i));
				}
				break;
			} else {
				retArr.add(new Move(pieceCol, i));
			}
		}
		// Loop for left moving
		for (int i = pieceCol - 1; i >= 0; i--) {	
			// If the new row index has a piece that is not empty
			if (Character.isAlphabetic(chessBoard[pieceRow][i].getType())) {
				// Check for if blocking piece is opposing piece, add to list if yes
				if (chessBoard[pieceRow][i].getColor() != curPiece.getColor()) {	
					retArr.add(new Move(i, pieceRow));
				}
				break;
			} else {
				retArr.add(new Move(i, pieceRow));
			}
		}
		// Loop for right moving
		for (int i = pieceCol + 1; i < this.cols; i++) {
			// If the new row index has a piece that is not empty
			if (Character.isAlphabetic(chessBoard[pieceRow][i].getType())) {
				// Check for if blocking piece is opposing piece, add to list if yes
				if (chessBoard[pieceRow][i].getColor() != curPiece.getColor()) {
					retArr.add(new Move(i, pieceRow));
				}
				break;
			} else {
				retArr.add(new Move(i, pieceRow));
			}
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	private ArrayList<Move> getMovesBishop(Piece curPiece) {
		ArrayList<Move> retArr = new ArrayList<Move>();
		int pieceRow = curPiece.getRank();
		int pieceCol = curPiece.getFile();
		// Loop for forward right diagonal
		for (int curRow = pieceRow-1, curCol = pieceCol+1; (0 <= curRow && curRow < this.rows) &&
				(0 <= curCol && curCol < this.cols); curRow--, curCol++) {
			if (Character.isAlphabetic(chessBoard[curRow][curCol].getType())) {
				// Check for if blocking piece is opposing piece, add to list if yes
				if (chessBoard[curRow][curCol].getColor() != curPiece.getColor()) {
					retArr.add(new Move(curCol, curRow));
				}
				break;
			} else {
				retArr.add(new Move(curCol, curRow));
			}
		}
		// Loop for forward left diagonal
		for (int curRow = pieceRow-1, curCol = pieceCol-1; (0 <= curRow && curRow < this.rows) &&
				(0 <= curCol && curCol < this.cols); curRow--, curCol--) {
			if (Character.isAlphabetic(chessBoard[curRow][curCol].getType())) {
				// Check for if blocking piece is opposing piece, add to list if yes
				if (chessBoard[curRow][curCol].getColor() != curPiece.getColor()) {
					retArr.add(new Move(curCol, curRow));
				}
				break;
			} else {
				retArr.add(new Move(curCol, curRow));
			}
		}
		// Loop for backward right diagonal
		for (int curRow = pieceRow+1, curCol = pieceCol+1; (0 <= curRow && curRow < this.rows) &&
				(0 <= curCol && curCol < this.cols); curRow++, curCol++) {
			if (Character.isAlphabetic(chessBoard[curRow][curCol].getType())) {
				// Check for if blocking piece is opposing piece, add to list if yes
				if (chessBoard[curRow][curCol].getColor() != curPiece.getColor()) {
					retArr.add(new Move(curCol, curRow));
				}
				break;
			} else {
				retArr.add(new Move(curCol, curRow));
			}
		}
		// Loop for backward left diagonal
		for (int curRow = pieceRow+1, curCol = pieceCol-1; (0 <= curRow && curRow < this.rows) &&
				(0 <= curCol && curCol < this.cols); curRow++, curCol--) {
			if (Character.isAlphabetic(chessBoard[curRow][curCol].getType())) {
				// Check for if blocking piece is opposing piece, add to list if yes
				if (chessBoard[curRow][curCol].getColor() != curPiece.getColor()) {
					retArr.add(new Move(curCol, curRow));
				}
				break;
			} else {
				retArr.add(new Move(curCol, curRow));
			}
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	private ArrayList<Move> getMovesQueen(Piece curPiece) {
		// Getting the possible diagonal moves
		ArrayList<Move> retArr = new ArrayList<Move>();
		ArrayList<Move> bMoves = getMovesBishop(curPiece);
		ArrayList<Move> rMoves =  getMovesRook(curPiece);
		if (bMoves != null) {
			retArr.addAll(bMoves);
		}
		if (rMoves != null) {
			retArr.addAll(rMoves);
		}
		// Adding possible vertical and horizontal moves to diagonal
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	public ArrayList<Move> getMovesKing(Piece king) {
		ArrayList<Move> temp = illegalMoveCheck(king);
		ArrayList<Move> moves = getMovesKingHelp(king); // get all possible moves for king
		if (moves == null) { // if no moves, return true
			return null;
		} 
		if (temp != null) {
			moves.removeAll(temp);
		}
		return moves;
	}
	
	private ArrayList<Move> getMovesKingHelp(Piece curPiece) {
		ArrayList<Move> kingMoveSet = new ArrayList<Move>();
		int pieceRow = curPiece.getRank();
		int pieceCol = curPiece.getFile();
		// First go in and add all possible moves for king with out "checkmate" checking
		// One above
		if (0 <= pieceRow+1 && pieceRow+1 < this.rows 
				&& chessBoard[pieceRow+1][pieceCol].getColor() != curPiece.getColor()) {
			kingMoveSet.add(new Move(pieceCol, pieceRow+1));
		}
		// One below
		if (0 <= pieceRow-1 && pieceRow-1 < this.rows 
				&& chessBoard[pieceRow-1][pieceCol].getColor() != curPiece.getColor()) {
			kingMoveSet.add(new Move(pieceCol, pieceRow-1));
		}
		// One left 
		if (0 <= pieceCol-1 && pieceCol-1 < this.cols 
				&& chessBoard[pieceRow][pieceCol-1].getColor() != curPiece.getColor()) {
			kingMoveSet.add(new Move(pieceCol-1, pieceRow));
		}
		// One right
		if (0 <= pieceCol+1 && pieceCol+1 < this.cols 
				&& chessBoard[pieceRow][pieceCol+1].getColor() != curPiece.getColor()) {
			kingMoveSet.add(new Move(pieceCol+1, pieceRow));
		}
		// One top right -- change from below
		if ((0 <= pieceRow-1 && pieceRow-1 < this.rows) &&
				(0 <= pieceCol+1 && pieceCol+1 < this.cols) 
				&& chessBoard[pieceRow-1][pieceCol+1].getColor() != curPiece.getColor()) {
			kingMoveSet.add(new Move(pieceCol+1, pieceRow-1));
		}
		// One top left 
		if ((0 <= pieceRow-1 && pieceRow-1 < this.rows) &&
				(0 <= pieceCol-1 && pieceCol-1 < this.cols) 
				&& chessBoard[pieceRow-1][pieceCol-1].getColor() != curPiece.getColor()) {
			kingMoveSet.add(new Move(pieceCol-1, pieceRow-1));
		}
		// One bottom right
		if ((0 <= pieceRow+1 && pieceRow+1 < this.rows) &&
				(0 <= pieceCol+1 && pieceCol+1 < this.cols)
				&& chessBoard[pieceRow+1][pieceCol+1].getColor() != curPiece.getColor()) {
			kingMoveSet.add(new Move(pieceCol+1, pieceRow+1));
		}
		// One bottom left
		if ((0 <= pieceRow+1 && pieceRow+1 < this.rows) &&
				(0 <= pieceCol-1 && pieceCol-1 < this.cols)
				&& chessBoard[pieceRow+1][pieceCol-1].getColor() != curPiece.getColor()) {
			kingMoveSet.add(new Move(pieceCol-1, pieceRow+1));
		}
		// Then iterate over all pieces in opposite team and create a set of all possible
		// moves by the opposing team
		ArrayList<Piece> oppPieces;
		if (Character.isUpperCase(curPiece.getType())) {
			oppPieces = this.blackPieces;
		} else {
			oppPieces = this.whitePieces;
		}
		for (Piece oppPiece : oppPieces) {
			ArrayList<Move> temp = null;
			int curCol = oppPiece.getFile();
			int curRow = oppPiece.getRank();
			// King detection is off because of stackoverflow
			// Pawn detection is different because pawns attack differently
			if (Character.toLowerCase(oppPiece.getType()) == 'p') {
				temp = pawnAttacks(oppPiece, oppPiece.getColor());
			} 
			// King detection is different, cannot move within 1 block radius of
			// other king
			else if (Character.toLowerCase(oppPiece.getType()) == 'k') {
				temp = new ArrayList<Move>();
				// Up
				temp.add(new Move(curCol, curRow-1));
				// Down
				temp.add(new Move(curCol, curRow+1));
				// Left
				temp.add(new Move(curCol-1, curRow));
				// Right
				temp.add(new Move(curCol+1, curRow));
				// Up Right Diagonal
				temp.add(new Move(curCol+1, curRow-1));
				// Up Left Diagonal
				temp.add(new Move(curCol-1, curRow-1));
				// Down Right Diagonal
				temp.add(new Move(curCol+1, curRow+1));
				// Down Left Diagonal
				temp.add(new Move(curCol-1, curRow+1));
			} else {
				temp = unknownPieceMoves(oppPiece);
			}
			if (temp != null && temp.size() > 0) {
				kingMoveSet.removeAll(temp);
			}
		}
		ArrayList<Move> retArr = new ArrayList<Move>(kingMoveSet);
//		printMoves(retArr);
		if (retArr.size() == 0) {
//			this.isGameOver = isCheckmate(curPiece); // if king has no moves, call isCheckmate.
			return null;
		} else {
//			this.isGameOver = false;
			return retArr;	
		}
	}
	
	private ArrayList<Move> unknownPieceMoves(Piece piece) {
		ArrayList<Move> retArr = null;
		char pType = Character.toLowerCase(piece.getType());
		if (pType == 'r') {
			retArr = getMovesRook(piece);
		} else if (pType == 'n') {
			retArr = getMovesKnight(piece);
		} else if (pType == 'b') {
			retArr = getMovesBishop(piece);
		} else if (pType == 'k') {
			retArr = getMovesKing(piece);
		} else if (pType == 'q') {
			retArr = getMovesQueen(piece);
		} else if (pType == 'p') {
			retArr = getMovesPawn(piece);
		}
		return retArr;
	}
	
	private ArrayList<Move> pawnAttacks(Piece piece, int color) {
		ArrayList<Move> retArr = new ArrayList<Move>();
		int col = piece.getFile();
		int row = piece.getRank();
		// If pawn is white
		if (color == 1 && Character.toLowerCase(piece.getType()) == 'p') {
			// Up Left Diagonal
			if ((0 <= row-1 && row-1 < this.rows) && (0 <= col-1 && col-1 < this.cols)) {
				retArr.add(new Move(col-1, row-1));
			}
			// Up Right Diagonal
			if ((0 <= row-1 && row-1 < this.rows) && (0 <= col+1 && col+1 < this.cols)) {
				retArr.add(new Move(col+1, row-1));
			}
		} 
		// If pawn is black
		else if (color == 2 && Character.toLowerCase(piece.getType()) == 'p') {
			// Up Left Diagonal
			if ((0 <= row+1 && row+1 < this.rows) && (0 <= col+1 && col+1 < this.cols)) {
				retArr.add(new Move(col+1, row+1));
			}
			// Up Right Diagonal
			if ((0 <= row+1 && row+1 < this.rows) && (0 <= col-1 && col-1 < this.cols)) {
				retArr.add(new Move(col-1, row+1));
			}
		}
		if (retArr.size() == 0) {
			return null;
		} else {
			return retArr;	
		}
	}
	
	/*
	 * This function checks if the king is in check or not. It checks the king's color that is being
	 * checked and sets an array list of pieces to the opposing pieces. Then it loops through each piece
	 * and checks the piece's type to determine which getMoves to call to set the opposing possible moves to equal.
	 * Then it checks if the opposing possible moves contains the king's spot returning true if it does, meaning 
	 * the king is in check.
	 */
	public boolean isCheck(Piece king) {
		ArrayList<Piece> oppPieces;
		if (king.getColor() == 1) {
			oppPieces = this.blackPieces;
		} else {
			oppPieces = this.whitePieces;
		}
		Move kingSpot = new Move(king.getFile(), king.getRank());
		
		for (Piece op : oppPieces) {
			ArrayList<Move> oppPossibleMoves;
			// pawn
			if (op.getType() == 'p' || op.getType() == 'P') {
				oppPossibleMoves = getMovesPawn(op);
				if (oppPossibleMoves != null && oppPossibleMoves.contains(kingSpot)) {
					return true;
				}
			}
			// rook
			if (op.getType() == 'r' || op.getType() == 'R') {
				oppPossibleMoves = getMovesRook(op);
				if (oppPossibleMoves != null && oppPossibleMoves.contains(kingSpot)) {
					return true;
				}
			}
			// knight
			if (op.getType() == 'n' || op.getType() == 'N') {
				oppPossibleMoves = getMovesKnight(op);
				if (oppPossibleMoves != null && oppPossibleMoves.contains(kingSpot)) {
					return true;
				}
			}
			// bishop
			if (op.getType() == 'b' || op.getType() == 'B') {
				oppPossibleMoves = getMovesBishop(op);
				if (oppPossibleMoves != null && oppPossibleMoves.contains(kingSpot)) {
					return true;
				}
			}
			// queen
			if (op.getType() == 'q' || op.getType() == 'Q') {
				oppPossibleMoves = getMovesQueen(op);
				if (oppPossibleMoves != null && oppPossibleMoves.contains(kingSpot)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList<Move> illegalMoveCheck(Piece king) {
		ArrayList<Move> illegalMoves = new ArrayList<Move>();
		ArrayList<Piece> oppPieces;
		if (king.getColor() == 1) {
			oppPieces = this.blackPieces;
		} else {
			oppPieces = this.whitePieces;
		}
		//Move kingSpot = new Move(king.getFile(), king.getRank());
		ArrayList<Move> kingMoves = getMovesKingHelp(king);
		if (kingMoves == null) {
			return null;
		}
		for (Move move : kingMoves) {
			for (Piece op : oppPieces) {
				ArrayList<Move> oppPossibleMoves;
				// pawn
//				if (op.getType() == 'p' || op.getType() == 'P') {
//					oppPossibleMoves = getMovesPawn(op);
//					if (oppPossibleMoves != null && oppPossibleMoves.contains(move)) {
//						illegalMoves.add(move);
//					}
//				}
				// rook
				if (op.getType() == 'r' || op.getType() == 'R') {
					oppPossibleMoves = getMovesRook(op);
					if (oppPossibleMoves != null && oppPossibleMoves.contains(move)) {
						illegalMoves.add(move);
					}
				}
				// knight
				if (op.getType() == 'n' || op.getType() == 'N') {
					oppPossibleMoves = getMovesKnight(op);
					if (oppPossibleMoves != null && oppPossibleMoves.contains(move)) {
						
					}
				}
				// bishop
				if (op.getType() == 'b' || op.getType() == 'B') {
					oppPossibleMoves = getMovesBishop(op);
					if (oppPossibleMoves != null && oppPossibleMoves.contains(move)) {
						illegalMoves.add(move);
					}
				}
				// queen
				if (op.getType() == 'q' || op.getType() == 'Q') {
					oppPossibleMoves = getMovesQueen(op);
					if (oppPossibleMoves != null && oppPossibleMoves.contains(move)) {
						illegalMoves.add(move);
					}
				}
			}
		}
		
		return illegalMoves;
	}
	
	/*
	 * This function checks if the king is in check. This is done by checking if
	 * it is the beginning of the game. Then making a list of all possible moves by king and
	 * making every move then calling isCheck to see if that move is putting king into check, 
	 * returning false if king would not be in check after that move. Returns true, if all
	 * moves are looped through and are all putting the king in check.
	 */
	public boolean isCheckmate(Piece king) {
		if (getFenString().equals(starterString)) {
			return false;
		} else {
			ArrayList<Move> moves = getMovesKingHelp(king); // get all possible moves for king
			if (moves == null) { // if no moves, return true
				return true;
			} else {
				Move kingSpot = new Move(king.getFile(), king.getRank()); // save king cur spot
				for (Move mv : moves) { // loop through every possible move king can make 
					Piece movePiece = chessBoard[mv.getY()][mv.getX()];
					isCheckmateHelper(kingSpot, mv); // make the move
					if (isCheck(chessBoard[mv.getX()][mv.getY()])) { // use isCheck to see if new spot is illegal
						isCheckmateHelper(mv, kingSpot); // if move puts king in check, reverse the move made 
						chessBoard[mv.getY()][mv.getX()] = movePiece;
					} else {
						isCheckmateHelper(mv, kingSpot); // else, reverse the move made for next move
						chessBoard[mv.getY()][mv.getX()] = movePiece;
						return false;
					}
				}
			}
			
		}
		return true;
	}
	
	// This is a helper function and does what makeMoves does except doesn't change the the graphic side
	// This is to make a move to check if the king would be in check after making that move, resulting in an illegal move
	// This function is only used in isCheckMate and is used to reverse what it was used for, each time it is used so that nothing is
	// actually changed in the board.
	public void isCheckmateHelper(Move oldMove, Move newMove) {
		Piece emptyReplacement = new Piece(' ', oldMove.getX(), oldMove.getY());
		chessBoard[newMove.getY()][newMove.getX()] = chessBoard[oldMove.getY()][oldMove.getX()];
		chessBoard[newMove.getY()][newMove.getX()].setX(newMove.getX());
		chessBoard[newMove.getY()][newMove.getX()].setY(newMove.getY());
		chessBoard[oldMove.getY()][oldMove.getX()] = emptyReplacement;
	}
	
	
	
	// ------------------------- End of new untested movement logic ---------------------------------
	
	private void printMoves(ArrayList<Move> list) {
		String print = "[";
		boolean first = true;
		for (Move m : list) {
			if (first) {
				print += "(" + m.getX() + "," + m.getY() + ")";
			} else {
				print += ", (" + m.getX() + "," + m.getY() + ")";
			}
		}
		System.out.println(print + "]");
	}

	public void makeMove(Move oldMove, Move newMove) {
		Piece emptyReplacement = new Piece(' ', oldMove.getX(), oldMove.getY());
		chessBoard[newMove.getY()][newMove.getX()] = chessBoard[oldMove.getY()][oldMove.getX()];
		chessBoard[newMove.getY()][newMove.getX()].setX(newMove.getX());
		chessBoard[newMove.getY()][newMove.getX()].setY(newMove.getY());
		chessBoard[oldMove.getY()][oldMove.getX()] = emptyReplacement;
		
		// checking if game is over after move is made
//		ArrayList<Piece> oppPieces;
//		char king;
//		if (chessBoard[oldMove.getY()][oldMove.getX()].getColor() == 1) {
//			oppPieces = this.blackPieces;
//			king = 'k';
//		} else {
//			oppPieces = this.whitePieces;
//			king = 'K';
//		}
//		for (Piece oppPiece : oppPieces) {
//			if (oppPiece.getType() == king) {
//				getMovesKing(oppPiece);
//				break;
//			}
//			
//		}
//		
//		if (isGameOver) {
//			
//		} else {
//			
//		}
		
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
				if (Character.isUpperCase(piece)) {
					this.whitePieces.add(newPiece);
				} else {
					this.blackPieces.add(newPiece);
				}
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
	
	public Piece getWhiteKing() {
		for (Piece piece : this.whitePieces) {
			if (piece.getType() == 'K') {
				return piece;
			}
		}
		return null;
	}
	
	public Piece getBlackKing() {
		for (Piece piece : this.blackPieces) {
			if (piece.getType() == 'k') {
				return piece;
			}
		}
		return null;
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
	
	public boolean getIsGameOver() {
		return this.isGameOver;
	}
}
