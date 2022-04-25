package view;

import java.util.HashMap;
import java.util.List;
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

public class SearchView extends GridPane {

	private TextField tfExactProperties;
	private TextField tfSameStartProperties;
	private TextField tfNestedEntityName;
	private TextField tfExactNestedProperties;
	private TextField tfSameStartNestedProperties;

	private Map<String, Object> exactMap;
	private Map<String, Object> sameStartMap;
	private Map<String, Object> exactNestedMap;
	private Map<String, Object> sameStartNestedMap;
	private String nestedEntityName;

	private Button btnSearch;

	private final TableView<Entity> tblEntities;

	private final DataHandlerSpecification dataHandlerSpecification;
	
	public SearchView(DataHandlerSpecification dataHandlerSpecification, TableView<Entity> tblEntities) {
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
		
		tfExactProperties = new TextField();
		tfSameStartProperties = new TextField();
		tfNestedEntityName = new TextField();
		tfExactNestedProperties = new TextField();
		tfSameStartNestedProperties = new TextField();
		btnSearch = new Button("Search");
	}

	private void add() {
		add(new Label("Exact properties"), 0, 0);
		add(tfExactProperties, 1, 0);
		add(new Label("Properties with the same start"), 0, 1);
		add(tfSameStartProperties, 1, 1);
		add(new Label("Name of the nested entity"), 0, 2);
		add(tfNestedEntityName, 1, 2);
		add(new Label("Exact nested properties"), 0, 3);
		add(tfExactNestedProperties, 1, 3);
		add(new Label("Nested properties with the same start"), 0, 4);
		add(tfSameStartNestedProperties, 1, 4);
		
		add(new Label("Format: property_name=property_value,property_name=property_value..."), 0, 5, 2, 1);
		
		add(btnSearch, 4, 6);
	}
	private void setOnAction() {
		btnSearch.setOnAction(e -> {
			exactMap = new HashMap<>();
			sameStartMap = new HashMap<>();
			exactNestedMap = new HashMap<>();
			sameStartNestedMap = new HashMap<>();
			nestedEntityName = "";

			if ((!tfExactNestedProperties.getText().trim().equals("") || !tfSameStartNestedProperties.getText().trim().equals("")) &&
					tfNestedEntityName.getText().trim().equals("")) {
				new Alert(AlertType.ERROR, "Name of the nested entity is required.").show();
				return;
			}

			findProperties(tfExactProperties, exactMap);
			findProperties(tfSameStartProperties, sameStartMap);
			findProperties(tfExactNestedProperties, exactNestedMap);
			findProperties(tfSameStartNestedProperties, sameStartNestedMap);

			if (!tfNestedEntityName.getText().trim().equals(""))
				nestedEntityName = tfNestedEntityName.getText().trim();


			List<Entity> entities = dataHandlerSpecification.search(exactMap, sameStartMap, nestedEntityName, exactNestedMap, sameStartNestedMap);
			tblEntities.setItems(FXCollections.observableArrayList(entities));
		});
	}

	private void findProperties(TextField textField, Map<String, Object> map) {
		if (!textField.getText().trim().equals("")) {
			String[] par = textField.getText().trim().split(",");
			for(String s : par) map.put(s.split("=")[0], s.split("=")[1]);
		}
	}

}

