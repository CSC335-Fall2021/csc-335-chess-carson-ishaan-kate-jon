package controller;

import model.ChessModel;
import model.Move;

public class ChessController {
	
	private ChessModel CurModel;
	
	public ChessController() {					// New Game
		this(null);		
	}
	
	public ChessController(String fenString) {	// Existing Game
		CurModel = new ChessModel(fenString);
	}
	
	public ChessModel getModel() {				// Retrieve chess Model
		return this.CurModel;
	}
	
	public String getFenString() {				// Retrieve encoded board
		return CurModel.getFenString();
	}
	
	public void makePlayerMove(Move oldMove, 
							   Move newMove) {	// Perform a player Move
		CurModel.makeMove(oldMove, newMove);
	}

}
