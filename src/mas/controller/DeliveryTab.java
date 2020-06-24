package mas.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import mas.model.DeliveryAddress;
import mas.model.TiresSwap;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeliveryTab implements Initializable {

    @FXML RadioButton deliverToMeRadio;
    @FXML RadioButton dontDeliverRadio;
    @FXML GridPane deliveryDetailsGridPane;
    @FXML TextField cityTextField;
    @FXML TextField streetTextField;
    @FXML TextField houseNumberTextfield;
    @FXML TextField flatNumberTextField;
    @FXML Button nextButton;
    @FXML Button backButton;
    @FXML Button cancelButton;

    // Patterns for data validation // TODO: polskie znaki
    Pattern cityPattern = Pattern.compile("^(\\w+[ ]?)+$");
    Pattern streetPattern = Pattern.compile("^(\\w+[ ]?)+$");
    Pattern houseNumberPattern = Pattern.compile("^[0-9]+[a-zA-Z]?$");
    Pattern flatNumberPattern = Pattern.compile("^[0-9]+$");

    private PageNavigationCallback pageNavigationCallback;
    private static DeliveryTab instance;
    private DeliveryAddress address;

    public DeliveryTab() {
        instance = this;
    }

    public static DeliveryTab getInstance() {
        return instance;
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

    public void setPageNavigationCallback(PageNavigationCallback pageNavigationCallback) {
        this.pageNavigationCallback = pageNavigationCallback;
    }
}
