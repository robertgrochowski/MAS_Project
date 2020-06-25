package mas.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import mas.model.DeliveryAddress;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for Delivery tab.
 *
 * @since 1.0
 * @author Robert Grochowski
 */
public class DeliveryTab implements Initializable {

    @FXML private RadioButton deliverToMeRadio;
    @FXML private RadioButton dontDeliverRadio;
    @FXML private GridPane deliveryDetailsGridPane;
    @FXML private TextField cityTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField houseNumberTextfield;
    @FXML private TextField flatNumberTextField;
    @FXML private Button nextButton;
    @FXML private Button backButton;
    @FXML private Button cancelButton;

    private static DeliveryTab instance;

    // Patterns for data validation // TODO: polskie znaki
    private final Pattern cityPattern = Pattern.compile("^(\\w+[ ]?)+$");
    private final Pattern streetPattern = Pattern.compile("^(\\w+[ ]?)+$");
    private final Pattern houseNumberPattern = Pattern.compile("^[0-9]+[a-zA-Z]?$");
    private final Pattern flatNumberPattern = Pattern.compile("^[0-9]+$");

    /**
     * PageNavigationCallback for calling the main controller about navigation callbacks (such as next tab)
     */
    private PageNavigationCallback pageNavigationCallback;

    /**
     * The delivery address. It may be null if client want to pickup the car in person.
     */
    private DeliveryAddress address;

    public DeliveryTab() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deliverToMeRadio.selectedProperty().addListener(
            (observableValue, wasPreviouslySelected, isNowSelected) -> {
                if(isNowSelected)
                    deliveryDetailsGridPane.setDisable(false);
            });
        dontDeliverRadio.selectedProperty().addListener(
            (observableValue, wasPreviouslySelected, isNowSelected) -> {
                if(isNowSelected) {
                    cityTextField.clear();
                    streetTextField.clear();
                    houseNumberTextfield.clear();
                    deliveryDetailsGridPane.setDisable(true);
                    address = null;
                }
            });

        nextButton.setOnAction(actionEvent -> {
            if(deliverToMeRadio.isSelected() && validate()) {
                address = new DeliveryAddress(cityTextField.getText().trim(),
                        streetTextField.getText().trim(),
                        houseNumberTextfield.getText().trim(),
                        flatNumberTextField.getText().trim().length() > 0 ? flatNumberTextField.getText().trim() : null);
                pageNavigationCallback.onNextPage();
            }
            else if(dontDeliverRadio.isSelected()) {
                pageNavigationCallback.onNextPage();
            }
        });

        backButton.setOnAction(actionEvent -> pageNavigationCallback.onPreviousPage());
        cancelButton.setOnAction(actionEvent -> pageNavigationCallback.onCancel());
    }

    public DeliveryAddress getDeliveryAddress(){
        return address;
    }

    /**
     * Validates the address input
     * @return  true  if the form is valid
     *          false if the form is invalid
     */
    public boolean validate() {
        if(dontDeliverRadio.isSelected())
            return true;

        StringBuilder errors = new StringBuilder();
        if(!cityPattern.matcher(cityTextField.getText().trim()).matches()) {
            errors.append("- Miasto; Poprawny format to słowa oddzielone spacją.").append('\n');
        }
        if(!streetPattern.matcher(streetTextField.getText().trim()).matches()) {
            errors.append("- Ulica; Poprawny format to słowa oddzielone spacją.").append('\n');
        }
        if(!houseNumberPattern.matcher(houseNumberTextfield.getText().trim()).matches()) {
            errors.append("- Numer domu; Poprawny format to liczba z opdjonalną literą (np. 2B)").append('\n');
        }
        if(!flatNumberPattern.matcher(flatNumberTextField.getText().trim()).matches() &&
                flatNumberTextField.getText().trim().length() > 0) {
            errors.append("- Numer mieszkania; Poprawny format to liczba lub pusta wartość").append('\n');
        }

        if(errors.length() > 0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "");
            alert.setTitle("Anuluj zamówienie");
            alert.setHeaderText("Wartości w następujących polach są niepoprawne:");
            alert.setContentText(errors.toString());
            alert.show();
            return false;
        }
        return true;
    }

    public static DeliveryTab getInstance() {
        return instance;
    }

    public void setPageNavigationCallback(PageNavigationCallback pageNavigationCallback) {
        this.pageNavigationCallback = pageNavigationCallback;
    }
}
