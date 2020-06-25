package mas.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mas.model.CarPart;
import mas.model.TechnicalRepair;
import mas.model.utils.Localization;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CarPartDialog implements Initializable {

    @FXML private TableView<CarPart> partsTable;
    @FXML private TableColumn<CarPart, String> nameCol;
    @FXML private TableColumn<CarPart, String> costCol;
    @FXML private TableColumn<CarPart, String> replaceTimeCol;
    @FXML private Button okButton;
    @FXML private Label repairNameLabel;
    private final ObservableList<CarPart> partsList = FXCollections.observableArrayList();

    public void setTechnicalRepair(TechnicalRepair technicalRepair) {
        partsList.addAll(technicalRepair.getCarParts());
        repairNameLabel.setText(technicalRepair.getDescription());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getName()));
        costCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format(Localization.CURRENCY_FORMAT, cell.getValue().getCost())));
        replaceTimeCol.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(String.format(Localization.DURATION_FORMAT,
                        cell.getValue().getAvgReplaceTime().toHours(),
                        cell.getValue().getAvgReplaceTime().toMinutes()%60)));

        okButton.setOnAction(a->close());
        partsTable.setItems(partsList);
    }

    private void close(){
        ((Stage)(partsTable.getScene().getWindow())).close();
    }

    public static void open(Stage rootStage, TechnicalRepair technicalRepair) {
        try {
            FXMLLoader loader = new FXMLLoader(CarPartDialog.class.getResource("/mas/view/comission/carpartDialog.fxml"));
            Parent root = loader.load();
            CarPartDialog controller = loader.getController();
            controller.setTechnicalRepair(technicalRepair);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMinWidth(root.minWidth(-1));
            stage.setMinHeight(root.minHeight(-1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(rootStage);
            stage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
