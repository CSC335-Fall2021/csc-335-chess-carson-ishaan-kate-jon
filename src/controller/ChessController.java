package controller;

import model.ChessModel;

public class ChessController {
	
	private ChessModel CurModel;
	
	public ChessController() {					// New Game
		this(null);		
	}
	
	public ChessController(String fenString) {	// Existing Game
		CurModel = new ChessModel(fenString);
	}
	
	public ChessModel getModel() {
		return this.CurModel;
	}
	
	public String getFenString() {
		return CurModel.getFenString();
	}

}
