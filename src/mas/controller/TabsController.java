package mas.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import mas.Main;
import mas.model.Service;
import mas.model.Ticket;
import mas.model.enums.TicketStatus;
import org.hibernate.Session;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for main window. It contains 3 tabs inside.
 * (Services tab, delivery tab and summary tab)
 * @see ServicesTab
 * @see DeliveryTab
 * @see SummaryTab
 *
 * @since 1.0
 * @author Robert Grochowski
 */

public class TabsController implements Initializable, PageNavigationCallback {
    @FXML private TabPane tabPane;
    @FXML private Tab servicesTab;
    @FXML private Tab deliveryTab;
    @FXML private Tab summaryTab;

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
        Tab switchToTab = tabsOrder[++currentTabIndex];
        switchToTab.setDisable(false);
        tabPane.getSelectionModel().select(switchToTab);
    }

    @Override
    public void onPreviousPage() {
        tabsOrder[currentTabIndex].setDisable(true);
        Tab switchToTab = tabsOrder[--currentTabIndex];
        tabPane.getSelectionModel().select(switchToTab);
    }

    @Override
    public void onCancel() {
        // TODO: move strings to Localization
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
        List<Service> servicesInOrder = servicesTabController.getCart();
        Ticket ticket = new Ticket(LocalDate.now(), null, deliveryTabController.getDeliveryAddress());

        // Add all services to ticket
        servicesInOrder.forEach(ticket::addServiceQualif);

        // Set status
        ticket.setStatus(TicketStatus.SUBMITTED);

        // Save
        // TODO: move strings to Localization
        try{
            Session session = Main.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(ticket);
            session.getTransaction().commit();
            session.close();

            ButtonType ok = new ButtonType("OK");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ok);
            alert.setTitle("Zgłoszenie przyjęte");
            alert.setHeaderText("Twoje zgłoszenie zostało przyjęte!");
            alert.setContentText("Dziękujemy za wybór naszej firmy.");
            alert.showAndWait();
            Platform.exit();
            System.exit(0);
        }
        catch (Exception e) {
            ButtonType ok = new ButtonType("OK");
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ok);
            alert.setTitle("Wystąpił błąd!");
            alert.setHeaderText("Wystąpił błąd podczas składania zamówienia.");
            alert.setContentText("Błąd: "+e.toString());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
