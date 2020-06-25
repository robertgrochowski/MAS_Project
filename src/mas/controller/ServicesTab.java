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
import mas.model.Cleaning;
import mas.model.Service;
import mas.model.TechnicalRepair;
import mas.model.TiresSwap;
import mas.model.utils.Localization;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Controller for Services Tab.
 *
 * @since 1.0
 * @author Robert Grochowski
 */
public class ServicesTab implements Initializable {

    /**
     * Model for FilterCombobox
     */
    private static class FilterColumnComboItem {
        private final TableColumn column;
        private final Class columnClass;

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

    ////// FXML Components
    @FXML private Button nextButton;
    @FXML private Button cancelButton;

    /// 1) Panel: Filters
    // 1a) Filter type
    @FXML private RadioButton technicalRepairTypeRadio;
    @FXML private RadioButton tiresSwapTypeRadio;
    @FXML private RadioButton cleaningTypeRadio;
    @FXML private Button clearTextField;
    // 1b) Filter column
    @FXML private ComboBox<FilterColumnComboItem> filterColumnComboBox;
    @FXML private TextField filterValueTextField;

    /// 2) Table: choose services
    // Common columns
    @FXML TableView<Service> chooseServicesTable;
    @FXML private TableColumn<Service, String> catalogueNumberCol;
    @FXML private TableColumn<Service, String> priceCol;
    @FXML private TableColumn<Service, String> realizationTimeCol;
    @FXML private TableColumn<Service, Service> selectCol;

    // 2a) Technical repair columns
    @FXML private TableColumn<TechnicalRepair, String> descriptionCol;
    @FXML private TableColumn<TechnicalRepair, TechnicalRepair> showPartsCol;

    // 2b) Tires swap columns
    @FXML private TableColumn<TiresSwap, String> tireSizeCol;
    @FXML private TableColumn<TiresSwap, Integer> tireManYearCol;
    @FXML private TableColumn<TiresSwap, String> tireTypeCol;

    // 2c) Cleaning columns
    @FXML private TableColumn<Cleaning, String> cleaningTypeCol;
    @FXML private TableColumn<Cleaning, String> carSizeCol;

    /// 3) Table: cart
    @FXML private TableView<Service> cartTable;
    @FXML private TableColumn<Service, String> catalogueNumberCartCol;
    @FXML private TableColumn<Service, String> detailsCartCol;
    @FXML private TableColumn<Service, String> priceCartCol;
    @FXML private TableColumn<Service, String> realizationTimeCartCol;
    @FXML private TableColumn<Service, Service> deleteFromCartCol;

    /// 4) Tab: summary
    @FXML private Label totalPriceLabel;
    @FXML private Label estimatedLeadTimeLabel;
    @FXML private Label chosenServicesAmountLabel;

    ////// Fields
    private static ServicesTab instance;

    /**
     * PageNavigationCallback for calling the main controller about navigation callbacks (such as next tab)
     */
    private PageNavigationCallback pageNavigationCallback;

    /**
     * Filter column combobox list model. It is filtered by chosen service type
     */
    private FilteredList<FilterColumnComboItem> filteredComboboxList;

    /**
     * Model for chooseServicesTable. It is filtered by chosen service type
     */
    private final FilteredList<Service> allServicesModel;

    /**
     * List model for cartTable
     */
    private final ObservableList<Service> cart = FXCollections.observableArrayList();

    /**
     * Actual filtered service type
     */
    private Class<? extends Service> filteredClass = TechnicalRepair.class;

    /**
     * Predicate for FilteredList allServicesModel
     * It filters actual selected type and items in cart
     */
    private final Predicate<Service> servicesModelTypePredicate;

    /**
     * Predicate for filteredComboboxList.
     * It filters actual selected type.
     */
    private Predicate<Service> columnFilterPredicate;

    // List of columns for specified service type
    private List<TableColumn> technicalRepairColumns;
    private List<TableColumn> tiresColumns;
    private List<TableColumn> cleaningColumns;

    public ServicesTab() {
        instance = this;
        cart.addListener((ListChangeListener<Service>) change -> {
            double totalPrice = cart.stream()
                    .map(Service::getPrice)
                    .reduce(0d, Double::sum);

            Duration totalDuration = cart.stream()
                    .map(Service::getEstimatedRealizationTime)
                    .reduce(Duration.ZERO, Duration::plus);

            totalPriceLabel.setText(String.format(Localization.TOTAL_PRICE_FORMAT, totalPrice));
            estimatedLeadTimeLabel.setText(String.format(Localization.ESTIMATED_REALIZATION_FORMAT, totalDuration.toHours(), totalDuration.minusHours(totalDuration.toHours()).toMinutes()));
            chosenServicesAmountLabel.setText(String.format(Localization.CHOSEN_SERVICES_FORMAT, cart.size()));
        });

        // Predicates and lists
        servicesModelTypePredicate = service -> service.getClass() == filteredClass && !cart.contains(service);
        columnFilterPredicate = service -> true; // No filters at start
        allServicesModel = new FilteredList<>(FXCollections.observableArrayList(Service.getExtent()), servicesModelTypePredicate);
    }

    /**
     * Refresh services table predicate as cart content or filtered service type may change
     */
    private void refreshServicesTable(){
        allServicesModel.setPredicate(e-> ( e.getClass() == filteredClass &&  !cart.contains(e)));
    }

    /**
     * Callback called when user changed the service type
     * (from radiobutton)
     */
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

    /**
     * Called whether client added a service to cart
     * @param service chosen service
     */
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fill column lists with their columns
        technicalRepairColumns = Arrays.asList(descriptionCol, showPartsCol);
        tiresColumns = Arrays.asList(tireSizeCol, tireManYearCol, tireTypeCol);
        cleaningColumns = Arrays.asList(cleaningTypeCol, carSizeCol);

        // Add listeners for radio buttons
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

        // Set model for both tables
        chooseServicesTable.setItems(allServicesModel);
        cartTable.setItems(cart);

        /// Column properties set
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

        /// Column Filter
        // Add all services to model
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
                    //todo: add other columns
                    columnFilterPredicate = service -> s.getCatalogueNumber().contains(newValue);

                return servicesModelTypePredicate.test(s) && columnFilterPredicate.test(s); });
        }));

        // Call methods for the first time in order to initialize
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

    public static ServicesTab getInstance() {
        return instance;
    }

    public ObservableList<Service> getCart() {
        return cart;
    }

    public void setPageNavigationCallback(PageNavigationCallback callback) {
        this.pageNavigationCallback = callback;
    }

    private Stage getStage(){
        return (Stage) chooseServicesTable.getScene().getWindow();
    }
}
