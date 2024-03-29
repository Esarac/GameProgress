package view;

import java.io.IOException;

import controller.ControlMenu;
import exception.ExistingElementException;
import exception.ImpossiblePercentageException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Manager;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));//FXML
			Parent root = (Parent) loader.load();
			root.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());//CSS
			primaryStage.getIcons().add(new Image("file:../../med/icon/Logo.png"));
			primaryStage.setTitle("Game Progress");
			primaryStage.setScene(new Scene(root, 800, 600));
			primaryStage.show();
			
			ControlMenu nextController = loader.getController();
			nextController.generate();
			nextController.saveData(primaryStage);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
