package mas.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mas.model.DeliveryAddress;
import mas.model.Service;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class SummaryTab implements Initializable {
    @FXML TableView<Service> summaryTable;
    @FXML TableColumn<Service, String> catalogueNumberCol;
    @FXML TableColumn<Service, String> priceCol;
    @FXML TableColumn<Service, String> realizationTimeCol;
    @FXML TableColumn<Service, String> detailsCol;

    @FXML Button cancelButton;
    @FXML Button backButton;
    @FXML Button confirmOrderButton;

    @FXML TitledPane deliveryDetailsPane;
    @FXML TitledPane deliverySelfCollectionPane;
    @FXML Label cityLabel;
    @FXML Label streetLabel;
    @FXML Label houseNumberLabel;
    @FXML Label flatNumberLabel;

    @FXML Label totalPriceLabel;
    @FXML Label estimatedLeadTimeLabel;
    @FXML Label chosenServicesAmountLabel;

    //todo: move to Localization
    private final static String TOTAL_PRICE_FORMAT = "SUMA: %.2f PLN";
    private final static String ESTIMATED_REALIZATION_FORMAT = "Szacowany czas realizacji: %dh %dmin";
    private final static String CHOSEN_SERVICES_FORMAT = "Ilość wybranych usług: %d";
    private static final String CURRENCY_FORMAT = "%.2fzł";
    private static SummaryTab instance;
    private PageNavigationCallback pageNavigationCallback;
    private DeliveryAddress deliveryAddress;
    private ObservableList<Service> cart;

    public SummaryTab() {
        instance = this;
    }

    public static SummaryTab getInstance() {
        return instance;
    }

    public void setPageNavigationCallback(PageNavigationCallback pageNavigationCallback) {
        this.pageNavigationCallback = pageNavigationCallback;
    }

    public void updateSummary(ObservableList<Service> cart, DeliveryAddress deliveryAddress) {
        this.cart = cart;
        this.deliveryAddress = deliveryAddress;
        summaryTable.setItems(cart);

        double totalPrice = this.cart.stream()
                .map(Service::getPrice)
                .reduce(0d, Double::sum);

        Duration totalDuration = this.cart.stream()
                .map(Service::getEstimatedRealizationTime)
                .reduce(Duration.ZERO, Duration::plus);

        totalPriceLabel.setText(String.format(TOTAL_PRICE_FORMAT, totalPrice));
        estimatedLeadTimeLabel.setText(String.format(ESTIMATED_REALIZATION_FORMAT, totalDuration.toHours(), totalDuration.minusHours(totalDuration.toHours()).toMinutes()));
        chosenServicesAmountLabel.setText(String.format(CHOSEN_SERVICES_FORMAT, this.cart.size()));

        if(deliveryAddress != null) {
            cityLabel.setText(deliveryAddress.getCity());
            streetLabel.setText(deliveryAddress.getStreet());
            houseNumberLabel.setText(deliveryAddress.getHomeNumber());
            flatNumberLabel.setText(deliveryAddress.getFlatNumber() == null ? "-" : deliveryAddress.getFlatNumber());
        }
        deliveryDetailsPane.setVisible(deliveryAddress != null);
        deliverySelfCollectionPane.setVisible(deliveryAddress == null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        catalogueNumberCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCatalogueNumber()));
        priceCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format(CURRENCY_FORMAT, cell.getValue().getPrice())));
        realizationTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEstimatedRealizationTime().toMinutes() + "min"));
        detailsCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().toString()));

        backButton.setOnAction(actionEvent -> pageNavigationCallback.onPreviousPage());
        confirmOrderButton.setOnAction(actionEvent -> pageNavigationCallback.onConfirmOrder());
        cancelButton.setOnAction(actionEvent -> pageNavigationCallback.onCancel());
    }

}
