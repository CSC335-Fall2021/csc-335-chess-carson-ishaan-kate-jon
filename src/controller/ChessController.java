package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Platform;
import model.ChessModel;
import model.ChessMoveMessage;
import model.Move;
import model.Piece;

/**
 * This class is put in place to connect the Chess view and the chess
 * model to each other. The controller will call various methods within
 * the model which will then notify its observer that it has changed. This
 * class has 2 constructors, one for creating a new game and one for loading
 * an existing game. If it is a new game, the controller will pass null through
 * as the string to indicate whether to use the starter string. 
 */
public class ChessController {

	private ChessModel CurModel;
	private Socket connection;
	public boolean isServer = false;
	private boolean isConnected = false;
	private boolean isPuzzle = false;
	private int puzzleNum = 0;
	private boolean puzzleWin = false;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;

	// Default Constructor
	public ChessController() { 
		this(null);
	}
	// Paramaterized Constructor
	public ChessController(String fenString) { 
		CurModel = new ChessModel(fenString);
	}
	
	/**
	 * This method is put in place to return the current Model of the chess game
	 * this method is used whenever additional information from the model is needed
	 * within the view after setChanged and notifyObservers runs. This method takes 
	 * in no parameters and returns the instance variable for the model.
	 */
	public ChessModel getModel() { // Retrieve chess Model
		return this.CurModel;
	}

	/**
	 * This method retrieves the current game in its fen-notation. This notation is
	 * a universal language for chess and this method is used in most of this game's
	 * core function. This method calls the model's getFenString() method and returns
	 * the string that the method generates.
	 */
	public String getFenString() { // Retrieve encoded board
		return CurModel.getFenString();
	}
	
	/**
	 * This method calls a getter function within the model and returns a boolean
	 * value if the game is over or not. Since this will be saved as an instance 
	 * variable within the model, only a getter function is necessary to retrieve
	 * the desired value.
	 */
	public boolean getIsGameOver() {
		return CurModel.getIsGameOver();
	}
	
	public void setPuzzle() {
		this.isPuzzle = true;
	}
	
	public void setPuzzleNum(int num) {
		this.puzzleNum = num;
	}

	/**
	 * This method is called within the view to make a move to the board. This method 
	 * will create a move for the chess game given the parameters and sends a message 
	 * to the other Socket within the running server. This is how the view is able to
	 * update for each individual user.
	 */
	public void makePlayerMove(Move oldMove, Move newMove) { // Perform a player Move
		CurModel.makeMove(oldMove, newMove);
		ChessMoveMessage message = new ChessMoveMessage(oldMove, newMove);
		sendMessage(message);
	}

	
	public boolean getPuzzleWin() {
		return this.puzzleWin;
	}
	
	public void makePuzzleMove(Move oldMove, Move newMove) { // Perform a player Move
		if (this.puzzleNum == 1) {
			if (oldMove.getX() == 3 && oldMove.getY() == 3 && newMove.getX() == 1 && newMove.getY() == 4) {
				this.puzzleWin = true;
			} 
		}
		if (this.puzzleNum == 2) {
			if (oldMove.getX() == 2 && oldMove.getY() == 5 && newMove.getX() == 7 && newMove.getY() == 0) {
				this.puzzleWin = true;
			}
		}
		if (this.puzzleNum == 3) {
			if (oldMove.getX() == 5 && oldMove.getY() == 6 && newMove.getX() == 0 && newMove.getY() == 1) {
				this.puzzleWin = true;
			}
		}
		if (this.puzzleNum == 4) {
			if (oldMove.getX() == 1 && oldMove.getY() == 5 && newMove.getX() == 1 && newMove.getY() == 1) {
				this.puzzleWin = true;
			}
		} 
		if (this.puzzleNum == 5) {
			if (oldMove.getX() == 7 && oldMove.getY() == 3 && newMove.getX() == 4 && newMove.getY() == 0) {
				this.puzzleWin = true;
			}
		}
		if (this.puzzleNum == 6) {
			if (oldMove.getX() == 4 && oldMove.getY() == 4 && newMove.getX() == 2 && newMove.getY() == 5) {
				this.puzzleWin = true;
			}
		}
//		CurModel.makeMove(oldMove, newMove);
//		ChessMoveMessage message = new ChessMoveMessage(oldMove, newMove);
//		sendMessage(message);
	}
	
	/**
	 * This method is called within the view and its goal is to retrieve all of the 
	 * possible moves the designated piece can make. This will be displayed within the
	 * by physically changing the color of the tiles for each move adding a new handler 
	 * to it. This new handler will then be sent through the makePlayerMove
	 */
	public ArrayList<Move> getPossibleMoves(int file, int rank) {
		return CurModel.getPossibleMoves(file, rank);
	}
	
	/**
	 * This method is a basic server startup method. This method is run only for the user
	 * who designated to be the server. It creates a SOCKET at port 4000 and establishes
	 * a connection. The method will then wait until the user playing as CLIENT has also
	 * connected to the game.
	 */
	public void startServer() {
		try {
			ServerSocket server = new ServerSocket(4000);
			//This blocks, and that's bad. But it's only a short block
			//so we won't solve it. Once the client connects, it will
			//unblock.
			connection = server.accept();
			
			//When we reach here, we have a client, so grab the
			//streams to do our message sending/receiving
			oos = new ObjectOutputStream(connection.getOutputStream());
			ois = new ObjectInputStream(connection.getInputStream());
			
			isServer = true;
			isConnected = true;
			CurModel.setMyTurn(true);
		}
		catch(IOException e) {
			System.err.println("Something went wrong with the network! " + e.getMessage());
		}
	}
	
	/**
	 * This is a method to return a puzzle within the various puzzles provided. Every one
	 * of these puzzles are one move away from a checkMate and it is the user's goal to 
	 * figure out what that move is 
	 */
	public String getPuzzleFenString(int i) {
		ArrayList<String> puzzles = CurModel.puzzles();
		return puzzles.get(i-1);
	}
	
	/**
	 * This method runs only when the user designated for client clicks the client button. 
	 * The server should have already been setup from the other user so all it should do is 
	 * establish a connection and wait for the platform.runlater function
	 */
	public void startClient() {
		try {
			connection = new Socket("localhost", 4000);
			
			isServer = false;
			isConnected = true;
			CurModel.setMyTurn(false);
			oos = new ObjectOutputStream(connection.getOutputStream());
			ois = new ObjectInputStream(connection.getInputStream());

			//A thread represents code that can be done concurrently
			//to other code. We have the "main" thread where our program
			//is happily running its event loop, and now we
			//create a second thread whose entire job it is to send
			//our message to the other program and to block (wait) for 
			//their response.
			Thread t = new Thread(() -> {
				try {
					ChessMoveMessage otherMsg = (ChessMoveMessage)ois.readObject();
					
					//The runLater method places aings that change UI
					//n event on the main
					//thread's event queue. All thelements must be done on the main thread. 
					Platform.runLater(() -> {
						CurModel.setMyTurn(true);
						CurModel.makeMove(otherMsg.getoldMove(), otherMsg.getnewMove());
					});
					//We let the thread die after receiving one message.
					//We'll create a new one on the next click.
				}
				catch(IOException | ClassNotFoundException e) {
					System.err.println("Something went wrong with serialization: " + e.getMessage());
				}
			});
			//This is when the thread begins running - so now. 
			t.start();
			//Even though this method is done, the code of the thread
			//keeps doing its job. We've just allowed the event handler
			//to return and the event loop to keep running.
		}
		catch(IOException e) {
			System.err.println("Something went wrong with the network! " + e.getMessage());
		}
	}
	
	/**
	 * This method is put in place to send a message to the other Socket within the current
	 * connection. The parameter of this function is of type ChessMoveMessage and contains everything
	 * needed to duplicate the runnind functions on each thread. 
	 */
	private void sendMessage(ChessMoveMessage msg) {
		if(!isConnected) { return; }
		
		//See the code above that does the same. Here we will send
		//a message and receive one in the new thread, avoiding
		//blocking the event loop.
		Thread t = new Thread(() -> {
			try {
				oos.writeObject(msg);
				oos.flush();
				
				ChessMoveMessage otherMsg = (ChessMoveMessage)ois.readObject();
				
				Platform.runLater(() -> {
					CurModel.setMyTurn(true);
					CurModel.makeMove(otherMsg.getoldMove(), otherMsg.getnewMove());
				});
			}
			catch(IOException | ClassNotFoundException e) {
				System.err.println("Something went wrong with serialization: " + e.getMessage());
			}
			
		});
		t.start();
		
	}
	
	public boolean isPuzzle() {
		return isPuzzle;
	}
	
	public boolean getIsConnected() {
		return isConnected;
	}
	
	/**
	 * This function picks a random move to make for AI locally. It uses Random to 
	 * pick a random piece out of the black pieces and checks if that piece has possible moves.
	 * It adds all possible moves into a list of moves and uses random to pick one from that and makes
	 * the move.
	 */
	public void makeRandomMove() {
		while (true) {
			Random random = new Random();
			int x = random.nextInt(16);
			Piece p = CurModel.blackPieces.get(x);
			if (!CurModel.getPossibleMoves(p.getFile(), p.getRank()).equals(null)) {
				ArrayList<Move> availableMoves = CurModel.getPossibleMoves(p.getFile(), p.getRank());
				Random random1 = new Random();
				int y = random1.nextInt(availableMoves.size());
				Move oldSpot = new Move(p.getFile(), p.getRank());
				Move newSpot = new Move(availableMoves.get(y).getX(), availableMoves.get(y).getY());
				makePlayerMove(oldSpot, newSpot);
				break;
			}
		}
		return;
	}
	

}