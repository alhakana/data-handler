package view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Entity;
import model.EntityIDOpadajuceComp;
import model.EntityNameOpadajuceComp;
import model.EntityNameRastuceComp;

import java.util.List;

public class SortView extends GridPane {
	private ComboBox<String> comboBox;
	private RadioButton rBtnAscending;
	private RadioButton rBtnDescending;
	private Button btnSort;

	private final TableView<Entity> tblEntities;

	public SortView(TableView<Entity> tblEntities) {
		this.tblEntities = tblEntities;

		initialize();
		add();
		setOnAction();
	}

	private void initialize() {
		setPadding(new Insets(35));
		setHgap(20);
		setVgap(20);
		
		comboBox = new ComboBox<>();
		comboBox.getItems().addAll("By name", "By id");
		comboBox.getSelectionModel().selectFirst();
		
		rBtnAscending = new RadioButton("Ascending");
		rBtnAscending.selectedProperty();
		rBtnDescending = new RadioButton("Descending");
		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().add(rBtnDescending);
		toggleGroup.getToggles().add(rBtnAscending);

		btnSort = new Button("Sort");

	}

	private void add() {
		add(new Label("Sort:"), 0, 0);
		add(comboBox, 1, 0);
		add(rBtnDescending, 0, 1);
		add(rBtnAscending, 1, 1);
		add(btnSort, 1, 2);
	}

	private void setOnAction() {
		btnSort.setOnAction(e -> {
			List<Entity> entities = tblEntities.getItems();

			if (rBtnDescending.isSelected()) {
				if (comboBox.getSelectionModel().getSelectedItem().equals("By name"))
					entities.sort(new EntityNameOpadajuceComp());
				else entities.sort(new EntityIDOpadajuceComp());
			} else {
				if (comboBox.getSelectionModel().getSelectedItem().equals("By id"))
					entities.sort(new EntityNameRastuceComp());
				else entities.sort(null);
			}

			tblEntities.setItems(FXCollections.observableArrayList(entities));
		});
	}

}
