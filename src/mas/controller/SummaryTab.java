package mas.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mas.model.DeliveryAddress;
import mas.model.Service;
import mas.model.utils.Localization;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

/**
 * Controller for Summary tab.
 *
 * @since 1.0
 * @author Robert Grochowski
 */
public class SummaryTab implements Initializable {

    @FXML private TableView<Service> summaryTable;
    @FXML private TableColumn<Service, String> catalogueNumberCol;
    @FXML private TableColumn<Service, String> priceCol;
    @FXML private TableColumn<Service, String> realizationTimeCol;
    @FXML private TableColumn<Service, String> detailsCol;

    @FXML private Button cancelButton;
    @FXML private Button backButton;
    @FXML private Button confirmOrderButton;

    @FXML private TitledPane deliveryDetailsPane;
    @FXML private TitledPane deliverySelfCollectionPane;
    @FXML private Label cityLabel;
    @FXML private Label streetLabel;
    @FXML private Label houseNumberLabel;
    @FXML private Label flatNumberLabel;

    @FXML private Label totalPriceLabel;
    @FXML private Label estimatedLeadTimeLabel;
    @FXML private Label chosenServicesAmountLabel;

    private static SummaryTab instance;

    /**
     * PageNavigationCallback for calling the main controller about navigation callbacks (such as next tab)
     */
    private PageNavigationCallback pageNavigationCallback;


    public SummaryTab() {
        instance = this;
    }

    public static SummaryTab getInstance() {
        return instance;
    }

    public void setPageNavigationCallback(PageNavigationCallback pageNavigationCallback) {
        this.pageNavigationCallback = pageNavigationCallback;
    }

    /**
     * Called by Main tab controller (TabsController) while this tab is about to be loaded
     * and sets the required data.
     *
     * @param cart all Services chosen
     * @param deliveryAddress delivery address (may be null)
     */

    public void updateSummary(ObservableList<Service> cart, DeliveryAddress deliveryAddress) {
        // Set model to the summary table
        summaryTable.setItems(cart);

        // Count total price
        double totalPrice = cart.stream()
                .map(Service::getPrice)
                .reduce(0d, Double::sum);

        // Count total duration
        Duration totalDuration = cart.stream()
                .map(Service::getEstimatedRealizationTime)
                .reduce(Duration.ZERO, Duration::plus);

        // Set summary details
        totalPriceLabel.setText(String.format(Localization.TOTAL_PRICE_FORMAT, totalPrice));
        estimatedLeadTimeLabel.setText(String.format(Localization.ESTIMATED_REALIZATION_FORMAT, totalDuration.toHours(), totalDuration.minusHours(totalDuration.toHours()).toMinutes()));
        chosenServicesAmountLabel.setText(String.format(Localization.CHOSEN_SERVICES_FORMAT, cart.size()));

        // Set deliver details
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
        priceCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format(Localization.CURRENCY_FORMAT, cell.getValue().getPrice())));
        realizationTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getEstimatedRealizationTime().toMinutes() + "min"));
        detailsCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().toString()));

        backButton.setOnAction(actionEvent -> pageNavigationCallback.onPreviousPage());
        confirmOrderButton.setOnAction(actionEvent -> pageNavigationCallback.onConfirmOrder());
        cancelButton.setOnAction(actionEvent -> pageNavigationCallback.onCancel());
    }

}
