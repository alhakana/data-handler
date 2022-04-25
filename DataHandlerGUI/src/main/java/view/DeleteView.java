package view;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Entity;
import spec.DataHandlerSpecification;

public class DeleteView extends GridPane {
	private TextField tfName;
	private TextField tfProperties;
	private Map<String, Object> propertyMap;
	private Button btnDelete;

	private final TableView<Entity> tblEntities;

	private final DataHandlerSpecification dataHandlerSpecification;

	public DeleteView(DataHandlerSpecification dataHandlerSpecification, TableView<Entity> tblEntities) {
		this.dataHandlerSpecification = dataHandlerSpecification;
		this.tblEntities = tblEntities;

		initialize();
		add();
		setOnAction();
	}



	private void initialize() {
		setPadding(new Insets(35));
		setHgap(20);
		setVgap(20);
		
		tfName = new TextField();
		tfProperties = new TextField();

		btnDelete = new Button("Delete");
	}

	private void add() {
		add(new Label("Name"), 0, 0);
		add(tfName, 1, 0);
		add(new Label("Properties"), 0, 1);
		add(tfProperties, 1, 1);
		
		add(new Label("Format for properties: property_name=property_value,property_name=property_value..."), 0, 2, 4, 1);
		
		add(btnDelete, 4, 3);
	}

	private void setOnAction() {
		btnDelete.setOnAction(e -> {
			propertyMap = new HashMap<>();
			if (!tfName.getText().trim().equals("")) {
				propertyMap.put("name", tfName.getText());
			}

			if (!tfProperties.getText().trim().equals("")) {
				String[] par = tfProperties.getText().trim().split(",");
				for(String s : par) propertyMap.put(s.split("=")[0], s.split("=")[1]);
			}

			if (dataHandlerSpecification.delete(propertyMap) > 0) {
				new Alert(AlertType.CONFIRMATION, "Successfully deleted entities.").show();
				tblEntities.setItems(FXCollections.observableArrayList(dataHandlerSpecification.getFileService().read()));
				tblEntities.refresh();
			}
			else new Alert(AlertType.ERROR, "Unsuccessfully deleted entities.").show();
		});
	}
}

