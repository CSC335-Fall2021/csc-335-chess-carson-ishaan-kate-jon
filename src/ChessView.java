

import java.util.Observable;
import java.util.Observer;

import controller.ChessController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Piece;

public class ChessView extends Application implements Observer{
	
	private Stage stage;
	private Scene scene;
	private BorderPane root;
	private GridPane chessGrid;
	private ChessController controller;
	private Color color1 = Color.INDIANRED;
	private Color color2 = Color.ANTIQUEWHITE;
			

	@Override
	public void start(Stage primaryStage) {
		try {	
			controller = new ChessController();
			controller.getModel().addObserver(this);
			
			buildBoard(primaryStage, controller.getFenString());

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		String curFenRep = (String) arg1;
		buildBoard(stage, curFenRep);
	}

	private void buildBoard(Stage stage, String fenRep) {
		TabPane tabPane = new TabPane();
		stage.setTitle("Chess");
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

		 Tab tab1 = new Tab("Board", gridpane);
		 Tab tab2 = new Tab("Customize", CreateCustomization(stage));

	    tabPane.getTabs().add(tab1);
	    tabPane.getTabs().add(tab2);
	    
	    VBox vBox = new VBox(tabPane);
		Scene scene = new Scene(vBox);
		
		this.stage = stage;
		
        this.stage.setScene(scene);
        this.stage.show();
	}

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
	}

	public static void main(String[] args) {
		launch(args);
	}

}
