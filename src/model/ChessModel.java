package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javafx.beans.InvalidationListener;
import java.util.Observable;
import java.util.Set;

public class ChessModel extends Observable {
	
	private boolean isGameOver;
	private boolean thisTurn = false;
	Piece[][] chessBoard;
	ArrayList<Piece> whitePieces;
	ArrayList<Piece> blackPieces;
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
	
	private ArrayList<Move> getMovesKing(Piece curPiece) {
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
		printMoves(retArr);
		if (retArr.size() == 0) {
			this.isGameOver = isCheckmate(curPiece); // if king has no moves, call isCheckmate.
			return null;
		} else {
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
	
	public boolean isCheckmate(Piece king) {
		if (getFenString().equals(starterString)) {
			return false;
		} else {
			ArrayList<Move> moves = getMovesKing(king);
			return moves.equals(null);
		}
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
