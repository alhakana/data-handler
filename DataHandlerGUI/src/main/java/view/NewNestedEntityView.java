package view;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import model.Entity;
import spec.DataHandlerSpecification;

public class NewNestedEntityView extends GridPane {
	private TextField tfName;
	private TextField tfProperties;

	private Map<String, Object> propertyMap;
	private final Integer parentId;

	private Button btnAdd;

	private final TableView<Entity> tblEntities;

	private final DataHandlerSpecification dataHandlerSpecification;


	public NewNestedEntityView(DataHandlerSpecification dataHandlerSpecification, TableView<Entity> tblEntities) {
		this.dataHandlerSpecification = dataHandlerSpecification;
		this.tblEntities = tblEntities;

		Entity entity = tblEntities.getSelectionModel().getSelectedItem();
		parentId = entity.getId();
		
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
		propertyMap = new HashMap<>();
		
		btnAdd = new Button("Add");

	}
	
	private void add() {
		add(new Label("Nested entity name"), 0, 0);
		add(tfName, 1, 0);
		add(new Label("Nested entity properties"), 0, 1);
		add(tfProperties, 1, 1);
		
		add(new Label("Format: property_name=property_value,property_name=property_value..."), 0, 3, 2, 1);
		
		add(btnAdd, 4, 4);
	}

	private void setOnAction() {
		btnAdd.setOnAction(e -> {
			String name;

			if (!tfName.getText().trim().equals("")) name = tfName.getText().trim();
			else {
				new Alert(AlertType.ERROR, "Name of the nested entity is required.");
				return;
			}

			if (!tfProperties.getText().trim().equals("")) {
				String[] par = tfProperties.getText().trim().split(",");
				for(String s : par) propertyMap.put(s.split("=")[0], s.split("=")[1]);
			} else {
				new Alert(AlertType.ERROR, "At least one property is required.");
				return;
			}

			if (!dataHandlerSpecification.createNested(parentId, name, propertyMap)) {
				new Alert(AlertType.ERROR, "Try with another id.").show();
				return;
			}

			tblEntities.setItems(FXCollections.observableArrayList(dataHandlerSpecification.getFileService().read()));
			tblEntities.refresh();
		});
	}
}

