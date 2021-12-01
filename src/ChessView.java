

import controller.ChessController;
import javafx.application.Application;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ChessView extends Application {
	
	private Stage base;
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
//			controller.getModel().addObserver(this);
			
			buildBoard(primaryStage);

	        // Setting vBox in the scene
//			scene = new Scene(vBox,345,320);
			
			// Initializing and Assigning Event Handler to scene
//			EventHandler<MouseEvent> mouseEvent1 = new MouseClick();
//			scene.setOnMouseClicked(mouseEvent1);
			
			// Setting and showing scene to user
//			base.setScene(scene);
//			base.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void buildBoard(Stage stage) {
		TabPane tabPane = new TabPane();

		stage.setTitle("Chess");
		GridPane gridpane = new GridPane();
		
		for( int x = 0; x < 8; x++) {
			for( int y = 0; y < 8; y++) {
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
				gridpane.add(rec, x,y);
		}
		}

		 Tab tab1 = new Tab("Board", gridpane);
		 Tab tab2 = new Tab("Customize", CreateCustomization(stage));

	    tabPane.getTabs().add(tab1);
	    tabPane.getTabs().add(tab2);
	    
	    VBox vBox = new VBox(tabPane);
		Scene scene = new Scene(vBox);
		
        stage.setScene(scene);
        stage.show();
		
		
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
		buildBoard(stage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
