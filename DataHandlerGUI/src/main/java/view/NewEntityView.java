package view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import model.Entity;
import spec.DataHandlerSpecification;
import java.util.HashMap;
import java.util.Map;

public class NewEntityView extends GridPane {
	private TextField tfId;
	private TextField tfName;
	private TextField tfProperties;
	private TextField tfNestedName;
	private TextField tfNestedProperties;

	private Map<String, Object> propertyMap;
	private Map<String, Object> nestedPropertyMap;

	private Button btnAdd;

	private final TableView<Entity> tblEntities;

	private final DataHandlerSpecification dataHandlerSpecification;
	
	public NewEntityView(DataHandlerSpecification dataHandlerSpecification, TableView<Entity> tblEntities) {
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
		
		tfId = new TextField();
		tfName = new TextField();
		tfProperties = new TextField();
		tfNestedName = new TextField();
		tfNestedProperties = new TextField();
		propertyMap = new HashMap<>();
		nestedPropertyMap = new HashMap<>();
		
		btnAdd = new Button("Add");
	}
	
	private void add() {
		add(new Label("Id"), 0, 0);
		add(tfId, 1, 0);
		add(new Label("Name"), 0, 1);
		add(tfName, 1, 1);
		add(new Label("Properties"), 0, 2);
		add(tfProperties, 1, 2);
		add(new Label("Nested entity name"), 0, 3);
		add(tfNestedName, 1, 3);
		add(new Label("Nested entity properties"), 0, 4);
		add(tfNestedProperties, 1, 4);
		
		add(new Label("Format: property_name=property_value,property_name=property_value..."), 0, 5, 2, 1);
		
		add(btnAdd, 4, 6);
	}

	private void setOnAction() {
		btnAdd.setOnAction(e -> {
			if (tfName.getText().trim().equals("")) {
				new Alert(AlertType.ERROR, "Entity name is required.").show();
				return;
			}

			String name = tfName.getText().trim();
			getProperties(tfProperties, propertyMap);

			Integer id = null;
			if (!tfId.getText().trim().equals("")) {
				try {
					id = Integer.parseInt(tfId.getText().trim());
				} catch (Exception ex) {
					new Alert(AlertType.ERROR, "Entity ID must be a number").show();
					return;
				}
			}

			String nestedName = "";
			if (!tfNestedName.getText().trim().equals("")) {
				nestedName = tfNestedName.getText().trim();

				if (!tfNestedProperties.getText().trim().equals(""))
					getProperties(tfNestedProperties, nestedPropertyMap);
				else {
					new Alert(AlertType.ERROR, "Properties of the nested entity are required.");
					return;
				}
			}

			if (id == null)	callCreateMethod(nestedName, name);
			else callCreateWithIdMethod(nestedName, name, id);

			tblEntities.setItems(FXCollections.observableArrayList(dataHandlerSpecification.getFileService().read()));
		});
	}

	private void callCreateWithIdMethod(String nestedName, String name, Integer id) {
		boolean error = false;
		if (nestedName.trim().equals("")) {
			if (propertyMap.isEmpty()) {
				if (!dataHandlerSpecification.create(id, name)) error = true;
			} else if (!dataHandlerSpecification.create(id, name, propertyMap)) error = true;

		} else {
			if (propertyMap.isEmpty()) {
				if (!dataHandlerSpecification.create(id, name, nestedName, nestedPropertyMap)) error = true;
			} else if (!dataHandlerSpecification.create(id, name, propertyMap, nestedName, nestedPropertyMap)) error = true;
		}

		if (error) new Alert(AlertType.ERROR, "Entity with written ID already exists.").show();

	}

	private void callCreateMethod(String nestedName, String name) {
		if (propertyMap.isEmpty()) {
			if (nestedName.trim().equals("")) dataHandlerSpecification.create(name);
			else dataHandlerSpecification.create(name, nestedName, nestedPropertyMap);
		} else {
			if (nestedName.trim().equals("")) dataHandlerSpecification.create(name, propertyMap);
			else dataHandlerSpecification.create(name, propertyMap, nestedName, nestedPropertyMap);
		}
	}

	private void getProperties(TextField textField, Map<String, Object> propertyMap) {
		if (!textField.getText().trim().equals("")) {
			String[] par = textField.getText().trim().split(",");
			for(String s : par) propertyMap.put(s.split("=")[0], s.split("=")[1]);
		}
	}

}
