

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import controller.ChessController;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Move;
import model.Piece;

public class ChessView extends Application implements Observer{
	
	private Stage stage;
	private Scene scene;
	private BorderPane root;
	private GridPane chessGrid;
	private ChessController controller;
	private Color color1 = Color.INDIANRED;
	private Color color2 = Color.ANTIQUEWHITE;
	private int player = 0;
	public boolean canClick = true;
	private Move prevPosition;
	private PathTransition pathTransitionAnimation;
	private boolean localPlay = false;

			
	/**
	* Starts the GUI view of the chess board and creates the controller, model,
	* builds the board, and calls handler setup functions
	*
	* @param  Stage primaryStage
	* @return  void
	*/
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {	
			this.localPlay = true;
			controller = new ChessController();
			controller.getModel().addObserver(this);
			
			buildBoard(primaryStage, controller.getFenString());
			setupHandlersOne();
			

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Helps setup the handlers for mouse events when a piece is clicked
	*
	* @return void

	*/
	private void setupHandlersOne() {
		if (!this.canClick && !localPlay) {
			return;
		}
		ObservableList<Node> stackList = chessGrid.getChildren();
		for (Node node : stackList) {
			StackPane curNode = (StackPane) node;
			EventHandler<MouseEvent> mouseEvent = new MouseEvent1();
			curNode.setOnMouseClicked(mouseEvent);
		}
		
	}
	/**
	* Updates the board to display the current piece setup
	* 
	* @return   void
	*/
	@Override
	public void update(Observable arg0, Object arg1) {
		String curFenRep = (String) arg1;
		this.canClick = controller.getModel().isMyTurn();
		buildBoard(stage, curFenRep);
		setupHandlersOne();

		
	}
	
	public void setPlayer(int player) {
		this.player = player;
	}
	
	public int getPlayer() {
		return this.player;
	}
	
	/**
	* Will accept a stage and fen-formatted string, and builds a board based
	* on the fen string setup passed in
	*
	* @param  Stage stage
	* @param  String fenRap
	* @return   void
	*/
	private void buildBoard(Stage stage, String fenRep) {
		TabPane tabPane = new TabPane();
		if (this.stage == null) {
			stage.setTitle("Chess");
		} else {
			stage.setTitle(this.stage.getTitle());
		}
		GridPane gridpane = new GridPane();
		
		for( int x = 0; x < 8; x++) {
			for( int y = 0; y < 8; y++) {
				StackPane stack = new StackPane();
				Rectangle rec = new Rectangle();
				rec.setWidth(100);
				rec.setHeight(100);
				if( x%2 == 0 && y%2 ==0) {
				rec.setFill(color1);
				} else if (x%2 != 0 && y%2 !=0){
					rec.setFill(color1);
				} else {
					rec.setFill(color2);
				}
				stack.getChildren().add(rec);
				gridpane.add(stack, x,y);
		
			}
		}
		
		if (fenRep != null) {
			int file = 0;
			int rank = 0;
			for (int i=0; i<fenRep.length(); i++) {
				char piece = fenRep.charAt(i);
				if (piece == '/') {
					file = 0;
					rank++;
				} else if (Character.isAlphabetic(piece)) {
					String unicode = getUnicodeValue(piece);
					Label label = new Label(unicode);
					label.setScaleX(5.0);
					label.setScaleY(5.0);
					label.setAlignment(Pos.CENTER_RIGHT);
					ObservableList<Node> stackList = gridpane.getChildren();
					for (Node node : stackList) {
						StackPane curNode = (StackPane) node;
						if (gridpane.getRowIndex(curNode) == rank && gridpane.getColumnIndex(curNode) == file) {
							curNode.getChildren().add(label);
						}
					}
					file++;
				} else if (!Character.isAlphabetic(piece)) {
					for (int j = 0; j < Integer.parseInt("" + piece); j++) {
						Label label = new Label("");
						ObservableList<Node> stackList = gridpane.getChildren();
						for (Node node : stackList) {
							StackPane curNode = (StackPane) node;
							if (gridpane.getRowIndex(curNode) == rank && gridpane.getColumnIndex(curNode) == file) {
								curNode.getChildren().add(label);
							}
						}
						file++;
					}
				}
			}
		}
		
		this.stage = stage;
		this.chessGrid = gridpane;

		 Tab tab1 = new Tab("Board", gridpane);
		 Tab tab2 = new Tab("Customize", CreateCustomization(this.stage));
		 Tab tab3 = new Tab("Network", setupNetwork());
		 Tab tab4 = new Tab("Puzzles", createPuzzle());

	    tabPane.getTabs().add(tab1);
	    tabPane.getTabs().add(tab2);
	    tabPane.getTabs().add(tab3);
	    tabPane.getTabs().add(tab4);
	    
	    VBox vBox = new VBox(tabPane);
		Scene scene = new Scene(vBox);
		
		
        this.stage.setScene(scene);
        this.stage.show();
        
 		
// 		// Checks for both kings
// 		Piece bKing = controller.getModel().getBlackKing();
// 		Piece wKing = controller.getModel().getWhiteKing();
// 		if (controller.getModel().getPossibleMoves(player, player)) {
// 			final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//			alert.setTitle("Message");
//			alert.setHeaderText("Message");
//			alert.setContentText("Black king is in checkmate, White has won!");
//			Platform.runLater(alert::showAndWait);
//			Platform.exit();
// 		} else if (controller.getModel().isCheckmate(wKing)) {
// 			final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//			alert.setTitle("Message");
//			alert.setHeaderText("Message");
//			alert.setContentText("White king is in checkmate, Black has won!");
//			Platform.runLater(alert::showAndWait);
//			Platform.exit();
// 		}
 		
	}

	/**
	* Creates a Vbox of the puzzle options (1-6) and creates buttons with events
	* associated with them that will create a new board when pressed with the puzzle
	*
	* @return    Node
	*/
	private Node createPuzzle() {
		VBox vbox = new VBox();
		
		Label label = new Label ("Instructions: Each of these puzzles is one move away from checkmate\nChoose a puzzle:\n");
		
	
		Button puzzle1 = new Button("Puzzle 1");
		Label label1 = new Label ("  Black to win");
		HBox hbox1 = new HBox();
		hbox1.getChildren().addAll(puzzle1, label1);
		
		Button puzzle2 = new Button("Puzzle 2");
		Label label2 = new Label ("  White to win");
		HBox hbox2 = new HBox();
		hbox2.getChildren().addAll(puzzle2, label2);
		
		Button puzzle3 = new Button("Puzzle 3");
		Label label3 = new Label ("  White to win");
		HBox hbox3 = new HBox();
		hbox3.getChildren().addAll(puzzle3, label3);
		
		
		Button puzzle4 = new Button("Puzzle 4");
		Label label4 = new Label ("  White to win");
		HBox hbox4 = new HBox();
		hbox4.getChildren().addAll(puzzle4, label4);
		
		Button puzzle5 = new Button("Puzzle 5");
		Label label5 = new Label ("  White to win");
		HBox hbox5 = new HBox();
		hbox5.getChildren().addAll(puzzle5, label5);
		
		Button puzzle6 = new Button("Puzzle 6");
		Label label6 = new Label ("  Black to win");
		HBox hbox6 = new HBox();
		hbox6.getChildren().addAll(puzzle6, label6);
		
		puzzle1.setOnAction(event -> {
			controller = new ChessController(controller.getPuzzleFenString(1));
			controller.getModel().addObserver(this);
			buildBoard(new Stage(), controller.getFenString());
			setupHandlersOne();
			controller.setPuzzle();
			controller.setPuzzleNum(1);
		});
		puzzle2.setOnAction(event -> {
			controller = new ChessController(controller.getPuzzleFenString(2));
			controller.getModel().addObserver(this);
			buildBoard(new Stage(), controller.getFenString());
			setupHandlersOne();
			controller.setPuzzle();
			controller.setPuzzleNum(2);
		});
		puzzle3.setOnAction(event -> {
			controller = new ChessController(controller.getPuzzleFenString(3));
			controller.getModel().addObserver(this);
			buildBoard(new Stage(), controller.getFenString());
			setupHandlersOne();
			controller.setPuzzle();
			controller.setPuzzleNum(3);
		});
		puzzle4.setOnAction(event -> {	
			controller = new ChessController(controller.getPuzzleFenString(4));
			controller.getModel().addObserver(this);
			buildBoard(new Stage(), controller.getFenString());
			setupHandlersOne();
			controller.setPuzzle();
			controller.setPuzzleNum(4);
		});
		puzzle5.setOnAction(event -> {
			controller = new ChessController(controller.getPuzzleFenString(5));
			controller.getModel().addObserver(this);
			buildBoard(new Stage(), controller.getFenString());
			setupHandlersOne();
			controller.setPuzzle();
			controller.setPuzzleNum(5);
		});
		puzzle6.setOnAction(event -> {	
			controller = new ChessController(controller.getPuzzleFenString(6));
			controller.getModel().addObserver(this);
			buildBoard(new Stage(), controller.getFenString());
			setupHandlersOne();
			controller.setPuzzle();
			controller.setPuzzleNum(6);
		});
		
		vbox.getChildren().add(label);
		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
		vbox.getChildren().add(hbox3);
		vbox.getChildren().add(hbox4);
		vbox.getChildren().add(hbox5);
		vbox.getChildren().add(hbox6);
		vbox.setSpacing(10);
		return vbox;
	
	}

	/**
	* Returns a string unicode value based on the char that is passed in. The char
	* will come from a fen-formatted string and will determine what piece should be
	* on the part of the board
	*
	* @param  char piece
	* @return      String
	*/
	private String getUnicodeValue(char piece) {
		if (piece == 'p') {
			return "\u265F";
		}
		else if (piece == 'P') {
			return "\u2659";
		}
		else if (piece == 'r') {
			return "\u265C";
		}
		else if (piece == 'R') {
			return "\u2656";
		}
		else if (piece == 'n') {
			return "\u265E";
		}
		else if (piece == 'N') {
			return "\u2658";
		}
		else if (piece == 'b') {
			return "\u265D";
		}
		else if (piece == 'B') {
			return "\u2657";
		}
		else if (piece == 'q') {
			return "\u265B";
		}
		else if (piece == 'Q') {
			return "\u2655";
		}
		else if (piece == 'k') {
			return "\u265A";
		}
		else if (piece == 'K') {
			return "\u2654";
		}
		else {
			return " ";
		}
	}

	/**
	* Will take a stage as a parameter and create a tab where you can customize
	* the color of the chess board. The user can input either color strings or hex
	* values and can only fill in one input field or both. If the color input is invalid
	* the color will not change.
	*
	* @param  Stage stage
	* @return   Vbox
	*/
	private VBox CreateCustomization(Stage stage) {
		VBox vbox = new VBox();
		
		Label label1 = new Label("  Enter your color:");
		TextField textfield1 = new TextField("");
		textfield1.setPromptText("Hex value or text");
		textfield1.setMaxWidth(200);
		
		Label label2 = new Label("  Enter opponent's color:");
		TextField textfield2 = new TextField("");
		textfield2.setPromptText("Hex value or text");
		textfield2.setMaxWidth(200);
		
		Button button = new Button("Change Color");
    	button.setOnMouseClicked(event -> {
    		ChangeColors(textfield1.getText(), textfield2.getText(), stage);
    			});
    	
		vbox.getChildren().add(label1);
		vbox.getChildren().add(textfield1);
		vbox.getChildren().add(label2);
		vbox.getChildren().add(textfield2);
		vbox.getChildren().add(button);
		vbox.setSpacing(10);
		return vbox;
	}

	/**
	* Creates the tab menu where the network setup happens. There is two buttons: 
	* "Start Client" and "Start Server"
	* 
	* @return    Vbox
	*/
	private VBox setupNetwork() {
		VBox netVbox = new VBox();
		
		Button startServer = new Button("Start Server");
		Button startClient = new Button("Start Client");
		Button playLocal   = new Button("Play Local");
		
		startServer.setOnAction(event -> {
			this.player = 1;
			this.localPlay = false;
			startClient.setDisable(true);
			startServer.setDisable(true);
			playLocal.setDisable(true);
			stage.setTitle("SERVER CHESS (Player 1)");
			controller.startServer();
		});
		
		startClient.setOnAction(event -> {
			this.player = 2;
			this.localPlay = false;
			startClient.setDisable(true);
			startServer.setDisable(true);
			playLocal.setDisable(true);
			stage.setTitle("CLIENT CHESS (Player 2)");
			canClick = false;
			controller.startClient();
		});
		playLocal.setOnAction(event -> {
			this.player = 0;
			this.localPlay = true;
			startClient.setDisable(true);
			startServer.setDisable(true);
			playLocal.setDisable(true);
		});
		
		netVbox.getChildren().add(startServer);
		netVbox.getChildren().add(startClient);
		netVbox.getChildren().add(playLocal);
		netVbox.setSpacing(10);
		return netVbox;
	}
	
	/**
	* Changes the colors of the board by performing checks for hex value and color strings and
	* changes the global values for color1 and color2 accordingly. if input is invalid, the
	* color will not be changed
	*
	* @param  text1
	* @param  text2
	* @param stage
	* @return    void
	*/
	private void ChangeColors(String text1, String text2, Stage stage) {
		try {
			Color temp1;
			if(text1.contains("0x")) {
				temp1 = Color.web(text1);
			}else{
				text1.toUpperCase();
				temp1 = Color.valueOf(text1);
			}
			color1 = temp1;
		}catch (Exception E) {
			//if theres an error with color it will go to default
		}
		try {
			Color temp2;
			if(text2.contains("0x")) {
				temp2 = Color.web(text1);
			}else{
				text2.toUpperCase();
				temp2 = Color.valueOf(text2);
			}
			color2 = temp2;
		}catch (Exception E) {
			//if theres an error with color it will go to default 
		}
		buildBoard(stage, controller.getFenString());
		setupHandlersOne();
	}
	
	/**
	* Sets whether or not the pieces on this board are clickable
	*
	* @param  boolean val
	* @return      void
	*/
	public void setCanClicked(boolean val) {
		this.canClick = val;
	}
	
	/**
	 * This class implements the first click of the turn, this will display to the 
	 * user all possible moves they can make by highlighting the certain positions
	 * on the board. This will be done with a helper method to update the handlers
	 * on the nodes within the gridpane.
	 */
	private class MouseEvent1 implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent arg0) {
			if (!canClick) {return;}
			StackPane curPane = (StackPane)arg0.getSource();
			System.out.println("This node was clicked (file: " + chessGrid.getColumnIndex(curPane) + ") (rank:" + chessGrid.getRowIndex(curPane) + ")");
			prevPosition = new Move(chessGrid.getColumnIndex(curPane), chessGrid.getRowIndex(curPane));
			ObservableList<Node> stackContents = curPane.getChildren();
			for (Node child : stackContents) {
				if (child instanceof Label) {
					Label curLabel = (Label)child;
					if (curLabel.getText() == " ") {
						return;
					}
					else if ((curLabel.getText().charAt(0) == Character.toUpperCase(curLabel.getText().charAt(0))) &&
							  (player == 1 || player == 0)) { // is server or manual play
						ArrayList<Move> moveLst = controller.getPossibleMoves(chessGrid.getColumnIndex(curPane), chessGrid.getRowIndex(curPane));
						setupHandlers2(moveLst, curPane);
						
					}
					else if ((curLabel.getText().charAt(0) == Character.toLowerCase(curLabel.getText().charAt(0))) &&
							  player == 2 || player == 0) { // is client or manual player
						ArrayList<Move> moveLst = controller.getPossibleMoves(chessGrid.getColumnIndex(curPane), chessGrid.getRowIndex(curPane));
						setupHandlers2(moveLst, curPane);
					}
				}
			}
		}
		
		/**
		* Setups the mouse events for making a move on the chess board and ensuring
		* that it is a valid move
		*
		* @param  ArrayList<Move> moveLst
		* @param  StackPane curPane
		* @return      void
		*/
		private void setupHandlers2(ArrayList<Move> moveLst, StackPane curPane) {
			if (moveLst == null) {
				buildBoard(stage, controller.getFenString());
				setupHandlersOne();
				return;
			}
			ObservableList<Node> stackList = chessGrid.getChildren();
			for (Node node : stackList) {
				boolean possibleMove = false;
				StackPane curNode = (StackPane) node;
				curNode.setDisable(true);
				for (Move curMove : moveLst) {
					if (chessGrid.getColumnIndex(curNode) == curMove.getX() &&  chessGrid.getRowIndex(curNode) == curMove.getY()) {
						curNode.setDisable(false);
						EventHandler<MouseEvent> secondEvent = new MouseEvent2();
						
						curNode.setOnMouseClicked(secondEvent);
						possibleMove = true;
					}
				}
				ObservableList<Node> children = curNode.getChildren();
				if ((children.get(0) instanceof Rectangle) && possibleMove == true) {
					((Rectangle) children.get(0)).setStyle("-fx-fill: green; -fx-stroke: black; -fx-stroke-width: 1;");
				}
				
			}
			
		}
	}
	
	/**
	* Sets up the animation of piece moving and assigns the animation to a global variable
	* to setup the delay before the move is added to the model. Parameters are the piece to be 
	* moved, the old x coordinate of the piece, the old y coordinate, the new x coordinate
	* and the new y coordinate of the piece  
	*
	* @param  Object pieceToMove
	* @param  int oldX
	* @param  int oldY
	* @param  int newX
	* @param  int newY
	* @return      void
	*/
	private void setupPathAnimation(Object pieceToMove, int oldX, int oldY, int newX, int newY) {
		
		Path path = new Path();
        path.getElements().add(new MoveTo(oldX, oldY));
        path.getElements().add(new LineTo((newX - oldX) * 100, (newY- oldY) * 100));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setPath(path);
        pathTransition.setNode((Node) pieceToMove);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(true);
        pathTransitionAnimation = pathTransition;
	}
	
	/**
	 * This class implements the move logic. This handler will be placed on a node 
	 * whenever there is a possible move to be made, therefore when this handle method
	 * runs, it will send a message to the model making a logical wove within the game
	 */
	private class MouseEvent2 implements EventHandler<MouseEvent> {

		/**
		* Sets up the animation of piece moving and plays the animation, performs a delay to make sure
		* the animation is visible. Adds the move to the model.
		*
		* @param  MouseEvent arg0
		* @return      void
		*/
		@Override
		public void handle(MouseEvent arg0) {
			int oldX = 0;
			int oldY = 0;
			Node node = null;
			for (int i =0; i < chessGrid.getChildren().size(); i++) {
				if(chessGrid.getColumnIndex(chessGrid.getChildren().get(i)) == prevPosition.getX() 
						&& chessGrid.getRowIndex(chessGrid.getChildren().get(i)) == prevPosition.getY()) {
					oldX = chessGrid.getColumnIndex(chessGrid.getChildren().get(i));
					oldY = chessGrid.getRowIndex(chessGrid.getChildren().get(i));
					node = (Node) ((StackPane) chessGrid.getChildren().get(i)).getChildren().get(1);

				}
			}
			
			StackPane curPane1 = (StackPane)arg0.getSource();

			Move newPosition = new Move(chessGrid.getColumnIndex(curPane1), chessGrid.getRowIndex(curPane1));
			setupPathAnimation(node, oldX, oldY, newPosition.getX(), newPosition.getY());
			pathTransitionAnimation.play();
			pathTransitionAnimation.setOnFinished(e -> {
				if (controller.isPuzzle()) {
					controller.makePuzzleMove(prevPosition, newPosition);
					if (controller.getPuzzleWin() ) {
						final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						final Scene scene = alert.getDialogPane().getScene();
						alert.setTitle("Message");
						alert.setHeaderText("Message");
						alert.setContentText("You Win!");
						Platform.runLater(alert::showAndWait);
					} else {
						final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						final Scene scene = alert.getDialogPane().getScene();
						alert.setTitle("Message");
						alert.setHeaderText("Message");
						alert.setContentText("You Lose!");
						Platform.runLater(alert::showAndWait);
					}
				} else {
					controller.makePlayerMove(prevPosition, newPosition);
					
				}
				if (getPlayer() == 1 || getPlayer() == 2) {
					setCanClicked(false);
				}
				else if (getPlayer() == 0) {
					setCanClicked(true);
				}
				if (!controller.getIsConnected()) {
					boolean check = false;
					while(check == false) {
						try {
							controller.makeRandomMove();
							check = true;
						} catch(Exception E) {
							check = false;
					}
					}
					setCanClicked(true);
				}
			});
		}
		
	}

}
