package view;

import java.util.List;
import java.util.Map;
import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Entity;
import spec.DataHandlerSpecification;

public class MainView extends BorderPane {
    private TableView<Entity> tblEntities;
    private final ObservableList<Entity> listEntities;

    private Button btnSearch;
    private Button btnCreate;
    private Button btnCreateNested;
    private Button btnSort;
    private Button btnDelete;
    private Button btnDeleteQuery;

    private final DataHandlerSpecification dataHandlerSpecification;

    public MainView(DataHandlerSpecification dataHandlerSpecification, List<Entity> listEntities) {
        this.dataHandlerSpecification = dataHandlerSpecification;
        this.listEntities = FXCollections.observableArrayList(listEntities);

        initialize();
        makeTable();
        add();
        addActions();
    }

    private void initialize() {
        setPadding(new Insets(35));

        btnSearch = new Button("Search");
        btnCreate = new Button("Create");
        btnCreateNested = new Button("Create nested");
        btnSort = new Button("Sort");
        btnDelete = new Button("Delete");
        btnDeleteQuery = new Button("Delete by");

        tblEntities = new TableView<>();
    }

    private void add() {
        HBox hb = new HBox();
        hb.setSpacing(40);
        hb.getChildren().addAll(btnSearch, btnCreate, btnCreateNested, btnSort, btnDelete, btnDeleteQuery);
        hb.setPadding(new Insets(40, 20, 20, 20));
        setAlignment(hb, Pos.CENTER);

        setBottom(hb);

        setCenter(tblEntities);
    }

    @SuppressWarnings("unchecked")
    private void makeTable() {
        TableColumn<Entity, Integer> colID = new TableColumn<>("id");
        TableColumn<Entity, String> colName = new TableColumn<>("name");
        TableColumn<Entity, String> colProperties = new TableColumn<>("properties");
        TableColumn<Entity, String> colEntities = new TableColumn<>("entities");

        colID.setMinWidth(100);
        colName.setMinWidth(100);
        colProperties.setMinWidth(250);
        colEntities.setMinWidth(120);

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProperties.setCellValueFactory(new PropertyValueFactory<>("properties"));
		colEntities.setCellValueFactory(new PropertyValueFactory<>("entities"));

        tblEntities.setItems(listEntities);
        tblEntities.getColumns().addAll(colID, colName, colProperties, colEntities);
    }

    private void addActions() {
        btnSearch.setOnAction(e -> {
            if (Main.stage2 != null)
                Main.stage2.close();
            Scene scene = new Scene(new SearchView(dataHandlerSpecification, tblEntities), 600, 400);
            makeNewStage(scene, "Search entities");
        });

        btnCreate.setOnAction(e -> {
            if (Main.stage2 != null)
                Main.stage2.close();
            Scene scene = new Scene(new NewEntityView(dataHandlerSpecification, tblEntities), 600, 400);
            makeNewStage(scene, "Make new entity");
        });

        btnCreateNested.setOnAction(e -> {
            if (tblEntities.getSelectionModel().getSelectedItem() == null) {
                new Alert(AlertType.ERROR, "You have to choose entity first.").show();
                return;
            }

            if (Main.stage2 != null)
                Main.stage2.close();
            Scene scene = new Scene(new NewNestedEntityView(dataHandlerSpecification, tblEntities), 600, 250);
            makeNewStage(scene, "Make new nested entity");
        });

        btnSort.setOnAction(e -> {
            Scene scene = new Scene(new SortView(tblEntities), 350, 200);
            makeNewStage(scene, "Sort entities");
        });

        btnDelete.setOnAction(e -> {
            Entity entity = tblEntities.getSelectionModel().getSelectedItem();
            if (entity == null)
                new Alert(AlertType.ERROR, "You have to choose entity first.").show();
            else if (dataHandlerSpecification.delete(entity.getId())) {
                new Alert(AlertType.CONFIRMATION, "Entity successfully deleted.").show();
                tblEntities.setItems(FXCollections.observableArrayList(dataHandlerSpecification.getFileService().read()));
                tblEntities.refresh();
            } else
                new Alert(AlertType.ERROR, "Entity unsuccessfully deleted.").show();
        });

        btnDeleteQuery.setOnAction(e -> {
            Scene scene = new Scene(new DeleteView(dataHandlerSpecification, tblEntities), 650, 250);
            makeNewStage(scene, "Delete entities");
        });

    }

    private void makeNewStage(Scene scene, String title) {
        Main.stage2 = new Stage();
        Main.stage2.setScene(scene);
        Main.stage2.setTitle(title);
        Main.stage2.show();
    }
}
