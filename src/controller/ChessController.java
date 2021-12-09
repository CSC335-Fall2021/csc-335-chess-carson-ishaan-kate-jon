package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Platform;
import model.ChessModel;
import model.ChessMoveMessage;
import model.Move;

public class ChessController {

	private ChessModel CurModel;
	private Socket connection;
	private boolean isServer = false;
	private boolean isConnected = false;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public ChessController() { // New Game
		this(null);
	}

	public ChessController(String fenString) { // Existing Game
		CurModel = new ChessModel(fenString);
	}

	public ChessModel getModel() { // Retrieve chess Model
		return this.CurModel;
	}

	public String getFenString() { // Retrieve encoded board
		return CurModel.getFenString();
	}
	
	public boolean getIsGameOver() {
		return CurModel.getIsGameOver();
	}

	public void makePlayerMove(Move oldMove, Move newMove) { // Perform a player Move
		CurModel.makeMove(oldMove, newMove);
		ChessMoveMessage message = new ChessMoveMessage(oldMove, newMove);
		sendMessage(message);
	}
	
	public ArrayList<Move> getPossibleMoves(int file, int rank) {
		return CurModel.getPossibleMoves(file, rank);
	}
	
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
	public String getPuzzleFenString(int i) {
		ArrayList<String> puzzles = CurModel.puzzles();
		return puzzles.get(i-1);
	}
	
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
	

}