import controller.ChessController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChessView extends Application {
	
	private Stage base;
	private Scene scene;
	private BorderPane root;
	private GridPane chessGrid;
	private ChessController controller;

	@Override
	public void start(Stage primaryStage) {
		try {	
			controller = new ChessController();
//			controller.getModel().addObserver(this);
			
			// Initializing GridPane
			base = primaryStage;
			chessGrid = new GridPane();
			chessGrid.setVgap(8);
			chessGrid.setHgap(8);
			chessGrid.setPadding(new Insets(8));
			chessGrid.setAlignment(Pos.CENTER);
			chessGrid.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//			setupGrid();
			
	        
	        // Setting vBox in the scene
//			scene = new Scene(vBox,345,320);
			
			// Initializing and Assigning Event Handler to scene
//			EventHandler<MouseEvent> mouseEvent1 = new MouseClick();
//			scene.setOnMouseClicked(mouseEvent1);
			
			// Setting and showing scene to user
			base.setScene(scene);
			base.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
