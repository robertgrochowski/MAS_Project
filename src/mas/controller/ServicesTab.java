package mas.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mas.model.*;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class ServicesTab implements Initializable {

    private static class FilterColumnComboItem {
        private Predicate<Service> predicate;
        private TableColumn column;
        private Class columnClass;

        public FilterColumnComboItem(TableColumn column, Class columnClass, Predicate<Service> predicate) {
            this.predicate = predicate;
            this.columnClass = columnClass;
            this.column = column;
        }

        public Class getColumnClass() {
            return columnClass;
        }

        public Predicate<Service> getPredicate() {
            return predicate;
        }

        public TableColumn getColumn() {
            return column;
        }

        @Override
        public String toString() {
            return getColumn().getText();
        }
    }
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
    private final static String TOTAL_PRICE_FORMAT = "SUMA: %.2f PLN";
    private final static String ESTIMATED_REALIZATION_FORMAT = "Szacowany czas realizacji: %dh %dmin";
    private final static String CHOSEN_SERVICES_FORMAT = "Ilość wybranych usług: %d";
    @FXML Label totalPriceLabel;
    @FXML Label estimatedLeadTimeLabel;
    @FXML Label chosenServicesAmountLabel;

    private final List<Service> allServices = new ArrayList<>();
    private final FilteredList<Service> allServicesModel;
    private final ObservableList<Service> cart = FXCollections.observableArrayList();
    private Class<? extends Service> filteredClass = TechnicalRepair.class;

    private final Predicate<Service> servicesModelTypePredicate;

    public ServicesTab() throws Exception {
        CarPart part1 = new CarPart("Olej silnikowy", 200, Duration.ofMinutes(20));
        CarPart part2 = new CarPart("Skrzynia biegów", 600, Duration.ofMinutes(20));
        CarPart part3 = new CarPart("Żarówki mijania (2x)", 200, Duration.ofMinutes(20));
        CarPart part4 = new CarPart("Żarówki drogowe (2x)", 200, Duration.ofMinutes(20));
        CarPart part5 = new CarPart("Układ wspomagania", 400, Duration.ofMinutes(20));
        CarPart part6 = new CarPart("Pasek rozrządu", 80, Duration.ofMinutes(20));
        allServices.add(new TechnicalRepair("RDL", 50, "Wymiana żarówek świateł mijania", Arrays.asList(part3)));
        allServices.add(new TechnicalRepair("RBL", 50, "Wymiana żarówek świateł drogowych", Arrays.asList(part4)));
        allServices.add(new TechnicalRepair("RPS", 300, "Wymiana układu wspomagania", Arrays.asList(part5)));
        allServices.add(new TechnicalRepair("RTB", 200, "Wymiana pasku rozrządu", Arrays.asList(part6)));
        allServices.add(new TechnicalRepair("RGB", 200, "Wymiana skrzyni biegów", Arrays.asList(part2)));
        allServices.add(new TechnicalRepair("ROR", 100, "Wymiana oleju", Arrays.asList(part1)));

        // Cleanings
        allServices.add(new Cleaning("CCXS",  Cleaning.CarSize.XS, Cleaning.Type.COMPREHENSIVE));
        allServices.add(new Cleaning("CCS",  Cleaning.CarSize.S, Cleaning.Type.COMPREHENSIVE));
        allServices.add(new Cleaning("CCM",  Cleaning.CarSize.M, Cleaning.Type.COMPREHENSIVE));
        allServices.add(new Cleaning("CCL", Cleaning.CarSize.L, Cleaning.Type.COMPREHENSIVE));
        allServices.add(new Cleaning("CCXL", Cleaning.CarSize.XL, Cleaning.Type.COMPREHENSIVE));
        allServices.add(new Cleaning("CIXS",  Cleaning.CarSize.XS, Cleaning.Type.INNER));
        allServices.add(new Cleaning("CIS",  Cleaning.CarSize.S, Cleaning.Type.INNER));
        allServices.add(new Cleaning("CIM",  Cleaning.CarSize.M, Cleaning.Type.INNER));
        allServices.add(new Cleaning("CIL",  Cleaning.CarSize.L, Cleaning.Type.INNER));
        allServices.add(new Cleaning("CIXL",  Cleaning.CarSize.XL, Cleaning.Type.INNER));
        allServices.add(new Cleaning("COXS",  Cleaning.CarSize.XS, Cleaning.Type.INNER));
        allServices.add(new Cleaning("COS",  Cleaning.CarSize.S, Cleaning.Type.INNER));
        allServices.add(new Cleaning("COM",  Cleaning.CarSize.M, Cleaning.Type.INNER));
        allServices.add(new Cleaning("COL",  Cleaning.CarSize.L, Cleaning.Type.INNER));
        allServices.add(new Cleaning("COXL",  Cleaning.CarSize.XL, Cleaning.Type.INNER));

        // Tires swap
        for(int size = 13; size < 23; size ++)
        {
            for (TiresSwap.SeasonType seasonType : TiresSwap.SeasonType.values()) {

                for(int i = 0; i < 5; i++)
                {
                    int year = LocalDate.now().getYear()-i;
                    char seasonCat = seasonType.toString().charAt(0);
                    String yearCat = Integer.toString(year).substring(2, 4);
                    allServices.add(new TiresSwap("T"+size+seasonCat+yearCat, seasonType, size, year));
                }
            }
        }

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

        //Filters
        servicesModelTypePredicate = service -> service.getClass() == filteredClass && !cart.contains(service);
        columnFilterPredicate = service -> true; // No filters at start
        allServicesModel = new FilteredList<>(FXCollections.observableArrayList(allServices), servicesModelTypePredicate);
    }

    private void refreshServicesTable(){
        //allServicesModel.clear();
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

        refreshServicesTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        technicalRepairColumns = Collections.singletonList(descriptionCol);
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
        priceCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format(CURRENCY_FORMAT, cell.getValue().getPrice())));
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
        priceCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format(CURRENCY_FORMAT, cell.getValue().getPrice())));
        realizationTimeCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEstimatedRealizationTime().toMinutes() + "min"));
        deleteFromCartCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));

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
                });
            }
        });

        refreshServicesTable();

        // Filters:

        // Add all services to model
        // Filtrowalne kolumny: nr kat, opis, wielkosc opon, wielkosc auta, typ czyszczenia
        List<FilterColumnComboItem> filterColumnComboItems = new ArrayList<>();
        filterColumnComboItems.add(new FilterColumnComboItem(catalogueNumberCol, Service.class,
                s -> s.getCatalogueNumber().contains(filterValueTextField.getText())));

        filterColumnComboItems.add(new FilterColumnComboItem(descriptionCol, TechnicalRepair.class,
                s -> ((TechnicalRepair) s).getDescription().contains(filterValueTextField.getText())));

        filterColumnComboItems.add(new FilterColumnComboItem(tireSizeCol, TechnicalRepair.class,
                s -> ((TiresSwap) s).getSize().toString().contains(filterValueTextField.getText())));
        filterColumnComboItems.add(new FilterColumnComboItem(tireManYearCol, TiresSwap.class,
                s -> ((TiresSwap) s).getYearOfManufacture().toString().contains(filterValueTextField.getText())));
        filterColumnComboItems.add(new FilterColumnComboItem(cleaningTypeCol, Cleaning.class,
                s -> Localization.getLocalizedStringType(((Cleaning) s).getType()).contains(filterValueTextField.getText())));
        filterColumnComboItems.add(new FilterColumnComboItem(carSizeCol, Cleaning.class,
                s-> ((Cleaning) s).getCarSize().toString().contains(filterValueTextField.getText())));

        filterColumnComboBox.setItems(new FilteredList<>(FXCollections.observableArrayList(filterColumnComboItems),
                fc -> fc.columnClass.isAssignableFrom(filteredClass)));


        filterValueTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            FilterColumnComboItem item = filterColumnComboBox.getValue();
            if(item == null){
                columnFilterPredicate = s -> true;
            };

            //filterColumnComboItems
            columnFilterPredicate = item.getPredicate();

            allServicesModel.setPredicate(s-> {
                System.out.println(s.toString());
                System.out.println("col pred: " + columnFilterPredicate.test(s));
                System.out.println("servicesModelTypePredicate pred: " + servicesModelTypePredicate.test(s));
                System.out.println("---");
                return servicesModelTypePredicate.test(s) && columnFilterPredicate.test(s); });
        }));
    }

    void onAddToCartClicked(Service service)
    {
        System.out.println(service);
        System.out.println(service.canHaveOnlyOneInCart());
        if(service.canHaveOnlyOneInCart())
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
    }
}
