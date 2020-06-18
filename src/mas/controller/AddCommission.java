package mas.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import mas.model.Service;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCommission implements Initializable, PageNavigationCallback {
    @FXML TabPane tabPane;
    @FXML Tab servicesTab;
    @FXML Tab deliveryTab;
    @FXML Tab summaryTab;

    private final Tab[] tabsOrder = new Tab[3];
    private int currentTabIndex = 0;
    private DeliveryTab deliveryTabController;
    private ServicesTab servicesTabController;
    private SummaryTab summaryTabController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabsOrder[0] = servicesTab;
        tabsOrder[1] = deliveryTab;
        tabsOrder[2] = summaryTab;

        for(int i = 1; i < tabsOrder.length; i++)
        {
            tabsOrder[i].setDisable(true);
        }
        servicesTabController = ServicesTab.getInstance();
        servicesTabController.setPageNavigationCallback(this);

        deliveryTabController = DeliveryTab.getInstance();
        deliveryTabController.setPageNavigationCallback(this);

        summaryTabController = SummaryTab.getInstance();
        summaryTabController.setPageNavigationCallback(this);

        tabPane.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            currentTabIndex = newValue.intValue();
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, tab, newValue) -> {
            if(newValue == summaryTab)
                summaryTabController.updateSummary(servicesTabController.getCart(), deliveryTabController.getDeliveryAddress());
        });
    }

    @Override
    public void onNextPage() {
        if(currentTabIndex + 1 >= tabsOrder.length)
        {
            //this should never happen
            return;
        }
        Tab switchToTab = tabsOrder[++currentTabIndex];
        switchToTab.setDisable(false);
        tabPane.getSelectionModel().select(switchToTab);
    }

    @Override
    public void onPreviousPage() {
        if(currentTabIndex - 1 < 0)
        {
            //this should never happen
            return;
        }
        tabsOrder[currentTabIndex].setDisable(true);
        Tab switchToTab = tabsOrder[--currentTabIndex];
        tabPane.getSelectionModel().select(switchToTab);
    }

    @Override
    public void onCancel() {
        ButtonType yes = new ButtonType("Tak");
        ButtonType no = new ButtonType("Nie");

        Alert alert = new Alert(Alert.AlertType.WARNING, "", no, yes);
        alert.setTitle("Anuluj zamówienie");
        alert.setHeaderText("Czy napewno chcesz anulować zamówienie?");
        alert.setContentText("Wszystkie dane dot. zamówienia zostną utracone");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no) == yes)
        {
            Platform.exit();
            System.exit(0);
        }
    }

    @Override
    public void onConfirmOrder() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setTitle("Zgłoszenie przyjęte");
        alert.setHeaderText("Twoje zgłoszenie zostało przyjęte!");
        alert.setContentText("Dziękujemy za wybór naszej firmy.");
        alert.showAndWait();
        Platform.exit();
        System.exit(0);
    }
}
