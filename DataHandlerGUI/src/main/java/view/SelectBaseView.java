package view;

import app.Main;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import model.Entity;
import spec.DataHandlerSpecification;
import spec.StorageManager;

import java.io.File;
import java.util.List;

public class SelectBaseView extends BorderPane {
	private Button btnSelectBase;

	private String path;
	private DataHandlerSpecification dataHandlerSpecification;
	private List<Entity> entities;
	
	public SelectBaseView() {
		initialize();
		add();
		setActions();
	}
	
	public void initialize() {
		setPadding(new Insets(35));
		btnSelectBase = new Button("Choose database");
	}
	
	private void add() {
		setCenter(btnSelectBase);
		btnSelectBase.setPadding(new Insets(7));
	}
	
	private void setActions() {
		btnSelectBase.setOnAction(e -> {
			DirectoryChooser dc = new DirectoryChooser();
			File file = dc.showDialog(null);

			if (file == null || !file.isDirectory()) return;
			else path = file.getAbsolutePath();
			
			dataHandlerSpecification = StorageManager.getExporter(path);
			entities = dataHandlerSpecification.getFileService().read();

			Main.stage.setScene(new Scene(new MainView(dataHandlerSpecification, entities), 700, 700));
			Main.stage.centerOnScreen();
		});
	}
}
