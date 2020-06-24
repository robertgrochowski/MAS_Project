package mas.controller;

import com.sun.istack.internal.NotNull;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mas.model.*;
import mas.model.utils.Localization;

import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

public class ServicesTab implements Initializable {

    private FilteredList<FilterColumnComboItem> filteredComboboxList;
    private static ServicesTab instance; //todo
    private PageNavigationCallback pageNavigationCallback;

    private static class FilterColumnComboItem {
        private TableColumn column;
        private Class columnClass;

        public FilterColumnComboItem(@NotNull TableColumn column, Class columnClass) {
            this.columnClass = columnClass;
            this.column = column;
        }

        public Class getColumnClass() {
            return columnClass;
        }

        public TableColumn getColumn() {
            return column;
        }

        @Override
        public String toString() {
            return getColumn().getText();
        }
    }

    // TAB: Services
    @FXML Button nextButton;
    @FXML Button cancelButton;

    /// Panel: Filters
    @FXML RadioButton technicalRepairTypeRadio;
    @FXML RadioButton tiresSwapTypeRadio;
    @FXML RadioButton cleaningTypeRadio;
    @FXML TextField catalogueNumberTextField;
    @FXML TextField descriptionTextField;
    @FXML Button clearTextField;
    //
    @FXML ComboBox<FilterColumnComboItem> filterColumnComboBox;
    @FXML TextField filterValueTextField;
    private Predicate<Service> columnFilterPredicate;

    /// Table: choose services
    // Common columns
    @FXML
    TableView<Service> chooseServicesTable;
    @FXML TableColumn<Service, String> catalogueNumberCol;
    @FXML TableColumn<Service, String> priceCol;
    @FXML TableColumn<Service, String> realizationTimeCol;
    @FXML TableColumn<Service, Service> selectCol;

    // Technical repair columns
    @FXML TableColumn<TechnicalRepair, String> descriptionCol;
    @FXML TableColumn<TechnicalRepair, TechnicalRepair> showPartsCol;
    private List<TableColumn> technicalRepairColumns;

    // Tires swap columns
    @FXML TableColumn<TiresSwap, String> tireSizeCol;
    @FXML TableColumn<TiresSwap, Integer> tireManYearCol;
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
    //todo: extract to localization
    private final static String TOTAL_PRICE_FORMAT = "SUMA: %.2f PLN";
    private final static String ESTIMATED_REALIZATION_FORMAT = "Szacowany czas realizacji: "+ Localization.DURATION_FORMAT;
    private final static String CHOSEN_SERVICES_FORMAT = "Ilość wybranych usług: %d";
    @FXML Label totalPriceLabel;
    @FXML Label estimatedLeadTimeLabel;
    @FXML Label chosenServicesAmountLabel;

    private final FilteredList<Service> allServicesModel;
    private final ObservableList<Service> cart = FXCollections.observableArrayList();
    private Class<? extends Service> filteredClass = TechnicalRepair.class;

    private final Predicate<Service> servicesModelTypePredicate;

    public ServicesTab() {
        instance = this;
        cart.addListener((ListChangeListener<Service>) change -> {
            double totalPrice = cart.stream()
                    .map(Service::getPrice)
                    .reduce(0d, Double::sum);

            Duration totalDuration = cart.stream()
                    .map(Service::getEstimatedRealizationTime)
                    .reduce(Duration.ZERO, Duration::plus);

            totalPriceLabel.setText(String.format(TOTAL_PRICE_FORMAT, totalPrice));
            estimatedLeadTimeLabel.setText(String.format(ESTIMATED_REALIZATION_FORMAT, totalDuration.toHours(), totalDuration.minusHours(totalDuration.toHours()).toMinutes()));
            chosenServicesAmountLabel.setText(String.format(CHOSEN_SERVICES_FORMAT, cart.size()));
        });

        //Filters
        servicesModelTypePredicate = service -> service.getClass() == filteredClass && !cart.contains(service);
        columnFilterPredicate = service -> true; // No filters at start
        allServicesModel = new FilteredList<>(FXCollections.observableArrayList(Service.getExtent()), servicesModelTypePredicate);
    }

    private void refreshServicesTable(){
        allServicesModel.setPredicate(e-> ( e.getClass() == filteredClass &&  !cart.contains(e)));
    }

    private void onChangedServiceTypeFilter(){
        tiresColumns.forEach(tableColumn -> tableColumn.setVisible(false));
        cleaningColumns.forEach(tableColumn -> tableColumn.setVisible(false));
        technicalRepairColumns.forEach(tableColumn -> tableColumn.setVisible(false));

        if(filteredClass == TiresSwap.class)
            tiresColumns.forEach(tableColumn -> tableColumn.setVisible(true));
        if(filteredClass == Cleaning.class)
            cleaningColumns.forEach(tableColumn -> tableColumn.setVisible(true));
        if(filteredClass == TechnicalRepair.class)
            technicalRepairColumns.forEach(tableColumn -> tableColumn.setVisible(true));

        filteredComboboxList.setPredicate(fc->fc.getColumnClass().isAssignableFrom(filteredClass));
        refreshServicesTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        technicalRepairColumns = Arrays.asList(descriptionCol, showPartsCol);
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

        // Common columns
        catalogueNumberCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCatalogueNumber()));
        priceCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format(Localization.CURRENCY_FORMAT, cell.getValue().getPrice())));
        realizationTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEstimatedRealizationTime().toMinutes() + "min"));
        selectCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));

        // Technical repair columns
        descriptionCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDescription()));

        // Tires swap columns
        tireSizeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSize()+"''"));
        tireTypeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(Localization.getLocalizedSeasonType(cell.getValue().getSeasonType())));
        tireManYearCol.setCellValueFactory(cell-> new ReadOnlyObjectWrapper<>(cell.getValue().getYearOfManufacture()));

        // Cleaning columns
        cleaningTypeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(Localization.getLocalizedStringType(cell.getValue().getType())));
        carSizeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCarSize()+""));

        // Cart columns
        catalogueNumberCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCatalogueNumber()));
        detailsCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().toString()));
        priceCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format(Localization.CURRENCY_FORMAT, cell.getValue().getPrice())));
        realizationTimeCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEstimatedRealizationTime().toMinutes() + "min"));
        deleteFromCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
        showPartsCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
        showPartsCol.setCellFactory(p -> new TableCell<TechnicalRepair, TechnicalRepair>() {
            private final Button selectButton = new Button("Pokaż");
            @Override
            protected void updateItem(TechnicalRepair repair, boolean empty) {
                super.updateItem(repair, empty);
                if(repair == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(selectButton);
                selectButton.setOnAction(a-> CarPartDialog.open(getStage(), repair));
            }
        });

        selectCol.setCellFactory(p -> new TableCell<Service, Service>() {
            private final Button selectButton = new Button("Wybierz");
            @Override
            protected void updateItem(Service service, boolean empty) {
                super.updateItem(service, empty);
                if(service == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(selectButton);
                selectButton.setOnAction(e -> onAddToCartClicked(service));
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
                    cart.remove(item);
                    refreshServicesTable();
                });
            }
        });

        // Filters:

        // Add all services to model
        // Filtrowalne kolumny: nr kat, opis, wielkosc opon, wielkosc auta, typ czyszczenia
        List<FilterColumnComboItem> filterColumnComboItems = new ArrayList<>();
        filterColumnComboItems.add(new FilterColumnComboItem(catalogueNumberCol, Service.class));
        filterColumnComboItems.add(new FilterColumnComboItem(descriptionCol, TechnicalRepair.class));
        filterColumnComboItems.add(new FilterColumnComboItem(tireSizeCol, TiresSwap.class));
        filterColumnComboItems.add(new FilterColumnComboItem(tireManYearCol, TiresSwap.class));
        filterColumnComboItems.add(new FilterColumnComboItem(cleaningTypeCol, Cleaning.class));
        filterColumnComboItems.add(new FilterColumnComboItem(carSizeCol, Cleaning.class));

        filteredComboboxList = new FilteredList<>(FXCollections.observableArrayList(filterColumnComboItems));
        filterColumnComboBox.setItems(filteredComboboxList);

        filterValueTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            FilterColumnComboItem item = filterColumnComboBox.getValue();

            allServicesModel.setPredicate(s->
            {
                if(item == null || newValue.isEmpty())
                    columnFilterPredicate = a -> true;
                else
                    columnFilterPredicate = service -> s.getCatalogueNumber().contains(newValue);//todo

                return servicesModelTypePredicate.test(s) && columnFilterPredicate.test(s); });
        }));

        onChangedServiceTypeFilter();
        refreshServicesTable();

        // Navigation buttons
        nextButton.setOnAction(actionEvent -> {
            if(cart.size() < 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Błąd");
                alert.setHeaderText("Twój koszyk jest pusty");
                alert.setContentText("Aby przejść dalej wybierz co najmniej jedną usługę z listy");
                alert.show();
            }
            else pageNavigationCallback.onNextPage();
        });
        cancelButton.setOnAction(actionEvent -> pageNavigationCallback.onCancel());
    }

    void onAddToCartClicked(Service service)
    {
        if(service.isHaveOnlyOneInCart())
        {
            boolean hasAlreadyThisServiceInCart = cart.stream()
                    .anyMatch(e->e.getClass() == service.getClass());

            if(hasAlreadyThisServiceInCart)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Błąd");
                alert.setHeaderText("Już posiadasz usługę tego typu w koszyku");
                alert.setContentText("Jeśli chcesz ją zmienić, usuń usługę z koszyka i spróbuj ponownie.");
                alert.show();
                return;
            }
        }
        cart.add(service);
        refreshServicesTable();
    }

    public static ServicesTab getInstance() {
        return instance;
    }

    public ObservableList<Service> getCart() {
        return cart;
    }

    public void setPageNavigationCallback(PageNavigationCallback callback)
    {
        this.pageNavigationCallback = callback;
    }

    private Stage getStage(){
        return (Stage) chooseServicesTable.getScene().getWindow();
    }
}
