/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Validators;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sk.stu.fiit.Main.Alerts;

/**
 *
 * @author adamf
 */
public class TourOfferValidator {
    
    /**
     * Validates the given text fields if they are empty and
     * if the price is of the double data type.
     * 
     * @param startPlace
     * @param destinationPlace
     * @param price
     * @param description
     * @return 
     */
    public static boolean validateTextInputs(TextField startPlace, TextField destinationPlace, TextField price, TextArea description) {
        if (startPlace.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_START_PLACE");
            return false;
        } else if (destinationPlace.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_DESTINATION_PLACE");
            return false;
        }
        else if (price.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_PRICE");
            return false;
        }
        else if (description.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_DESCRIPTION");
            return false;
        } else if (isDouble(price)) {
            Alerts.showAlert("TITLE_PRICE_NOT_NUMBER");
            return false;
        }
        return true;
    }
    
    /**
     * Validates if the price is of the double data type.
     * 
     * @param price
     * @return boolean
     */
    public static boolean isDouble(TextField price) {
        try {
            Double.parseDouble(price.getText());
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
