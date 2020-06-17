package mas.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mas.model.*;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServicesTab implements Initializable {
    private static final String CURRENCY_FORMAT = "%.2fzł";
    // TAB: Services
    @FXML Button nextButtonServices;
    @FXML Button cancelButtonServices;

    /// Panel: Filters
    @FXML RadioButton technicalRepairTypeRadio;
    @FXML RadioButton tiresSwapTypeRadio;
    @FXML RadioButton cleaningTypeRadio;
    @FXML TextField catalogueNumberTextField;
    @FXML TextField descriptionTextField;
    @FXML Button clearTextField;

    /// Table: choose services
    // Common columns
    @FXML
    TableView<Service> chooseServicesTable;
    @FXML TableColumn<Service, String> catalogueNumberCol;
    @FXML TableColumn<Service, String> descriptionCol;
    @FXML TableColumn<Service, String> priceCol;
    @FXML TableColumn<Service, String> realizationTimeCol;
    @FXML TableColumn<Service, Service> selectCol;
    // Tires swap columns
    @FXML TableColumn<TiresSwap, String> tireSizeCol;
    @FXML TableColumn<TiresSwap, String> tireManYearCol;
    @FXML TableColumn<TiresSwap, String> tireTypeCol;
    private List<TableColumn> tiresColumns;
    // Cleaning columns
    @FXML TableColumn<Cleaning, String> cleaningTypeCol;
    @FXML TableColumn<Cleaning, String> carSizeCol;
    private List<TableColumn> cleaningColumns;

    ///Table: cart
    @FXML TableView<Service> tableCart;
    @FXML TableColumn<Service, String> catalogueNumberCartCol;
    @FXML TableColumn<Service, String> detailsCartCol;
    @FXML TableColumn<Service, String> priceCartCol;
    @FXML TableColumn<Service, String> realizationTimeCartCol;
    @FXML TableColumn<Service, Service> deleteFromCartCol;

    ///Panel: Summary
    private final static String TOTAL_PRICE_FORMAT = "SUMA: %.2f PLN";
    private final static String ESTIMATED_REALIZATION_FORMAT = "Szacowany czas realizacji: %dh %dmin";
    private final static String CHOSEN_SERVICES_FORMAT = "Ilość wybranych usług: %d";
    @FXML Label totalPriceLabel;
    @FXML Label estimatedLeadTimeLabel;
    @FXML Label chosenServicesAmountLabel;

    private final List<Service> allServices = new ArrayList<>();
    private final ObservableList<Service> allServicesModel = FXCollections.observableArrayList(); //<TechnicalService> ??
    //private final ObservableList<Service> cleaningRepairsModel = FXCollections.observableArrayList(); //<TechnicalService> ??
    private final ObservableList<Service> cart = FXCollections.observableArrayList();
    private Class filteredClass = TechnicalRepair.class;

    public ServicesTab() throws Exception {
        CarPart part1 = new CarPart("Olej silnikowy", 200, Duration.ofMinutes(20));
        CarPart part2 = new CarPart("Skrzynia biegów", 200, Duration.ofMinutes(20));
        CarPart part3 = new CarPart("Reflektor", 200, Duration.ofMinutes(20));
        allServices.add(new TechnicalRepair("1A", "Wymiana halogenów", 200, Arrays.asList(part3)));
        allServices.add(new TechnicalRepair("2C", "Wymiana skrzyni biegów", 200, Arrays.asList(part2)));
        allServices.add(new TechnicalRepair("4A", "Wymiana oleju", 100, Arrays.asList(part1)));

        allServicesModel.addAll(allServices);

        cart.addListener((ListChangeListener<Service>) change -> {
            double totalPrice = cart.stream()
                    .map(Service::getPrice)
                    .reduce(0d, Double::sum);

            Duration totalDuration = cart.stream()
                    .map(Service::getEstimatedRealizationTime)
                    .reduce(Duration.ZERO, Duration::plus);

            totalPriceLabel.setText(String.format(TOTAL_PRICE_FORMAT, totalPrice));
            estimatedLeadTimeLabel.setText(String.format(ESTIMATED_REALIZATION_FORMAT, totalDuration.toHours(), totalDuration.toMinutes()));
            chosenServicesAmountLabel.setText(String.format(CHOSEN_SERVICES_FORMAT, cart.size()));
        });
    }

    private void refreshServicesTable(){
        allServicesModel.clear();
        allServicesModel.addAll(allServices.stream()
                .filter(e-> e.getClass() == filteredClass)
                .filter(e-> !cart.contains(e))
                .collect(Collectors.toList()));
    }

    private void onChangedServiceTypeFilter(){
        tiresColumns.forEach(tableColumn -> tableColumn.setVisible(false));
        cleaningColumns.forEach(tableColumn -> tableColumn.setVisible(false));

        if(filteredClass == TiresSwap.class)
            tiresColumns.forEach(tableColumn -> tableColumn.setVisible(true));
        if(filteredClass == Cleaning.class)
            cleaningColumns.forEach(tableColumn -> tableColumn.setVisible(true));

        refreshServicesTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tiresColumns = Arrays.asList(tireSizeCol, tireManYearCol, tireTypeCol);
        cleaningColumns = Arrays.asList(cleaningTypeCol, carSizeCol);

        technicalRepairTypeRadio.selectedProperty().addListener((observableValue, wasPreviouslySelected, isNowSelected) -> {
            if(isNowSelected)
                filteredClass = TechnicalRepair.class;
            onChangedServiceTypeFilter();
        });

        tiresSwapTypeRadio.selectedProperty().addListener((observableValue, wasPreviouslySelected, isNowSelected) -> {
            if(isNowSelected)
                filteredClass = TiresSwap.class;
            onChangedServiceTypeFilter();
        });

        cleaningTypeRadio.selectedProperty().addListener((observableValue, wasPreviouslySelected, isNowSelected) -> {
            if(isNowSelected)
                filteredClass = Cleaning.class;
            onChangedServiceTypeFilter();
        });

        chooseServicesTable.setItems(allServicesModel);
        tableCart.setItems(cart);

        catalogueNumberCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCatalogueNumber()));
        catalogueNumberCartCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCatalogueNumber()));
        descriptionCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        detailsCartCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().toString()));
        priceCol.setCellValueFactory(cell -> new SimpleStringProperty(String.format(CURRENCY_FORMAT, cell.getValue().getPrice())));
        priceCartCol.setCellValueFactory(cell -> new SimpleStringProperty(String.format(CURRENCY_FORMAT, cell.getValue().getPrice())));
        realizationTimeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstimatedRealizationTime().toMinutes() + "min"));
        realizationTimeCartCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstimatedRealizationTime().toMinutes() + "min"));
        selectCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
        deleteFromCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
        selectCol.setCellFactory(p -> new TableCell<Service, Service>() {
            private final Button selectButton = new Button("Wybierz");
            @Override
            protected void updateItem(Service item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(selectButton);
                selectButton.setOnAction(e -> {
                    allServicesModel.remove(item);
                    cart.add(item);
                });
            }
        });

        deleteFromCartCol.setCellFactory(p -> new TableCell<Service, Service>() {
            private final Button deleteButton = new Button("Usuń");
            @Override
            protected void updateItem(Service item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(e -> {

                    if(item.getClass() == filteredClass)
                        allServicesModel.add(item);
                    cart.remove(item);
                });
            }
        });
    }
}
