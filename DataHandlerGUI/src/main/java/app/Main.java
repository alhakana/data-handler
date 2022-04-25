package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.SelectBaseView;

public class Main extends Application {
	public static Stage stage;
	public static Stage stage2;
	
	public static void main(String[] args) {
		try {
			Class.forName("app.JsonRegister");
//			Class.forName("app.YamlRegister");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
		
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		stage = primaryStage;
		stage.setScene(new Scene(new SelectBaseView(), 350, 150));
		stage.setTitle("DataHandler");
		stage.show();
	}
}
