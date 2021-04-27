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

    public static boolean validateTextInputs(TextField tf1, TextField tf2, TextField tf3, TextArea ta) {
        if (tf1.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_START_PLACE");
            return false;
        } else if (tf2.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_DESTINATION_PLACE");
            return false;
        }
        else if (tf3.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_PRICE");
            return false;
        }
        else if (ta.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_DESCRIPTION");
            return false;
        } else if (isDouble(tf3)) {
            Alerts.showAlert("TITLE_PRICE_NOT_NUMBER");
            return false;
        }
        return true;
    }
    
    public static boolean isDouble(TextField price) {
        try {
            Double.parseDouble(price.getText());
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}


/*
public interface TourOfferValidator 
        extends Function<TourCreate, ValidationResult> {
    
    static TourOfferValidator isStartPlaceEmpty() {
        return tourCreate -> tourCreate.getStartPlace().isEmpty() ? 
                START_PLACE_IS_EMPTY : SUCCESS;
    }
    
    static TourOfferValidator isDestinationPlaceEmpty() {
        return tourCreate -> tourCreate.getDestinationPlace().isEmpty() ? 
                DESTINATION_PLACE_IS_EMPTY : SUCCESS;
    }
    
    static TourOfferValidator isPricePerPersonEmpty() {
        return tourCreate -> tourCreate.getPricePerPerson() == 0.0 ? 
                PRICE_PER_PERSON_IS_EMPTY : SUCCESS;
    }
    
    static TourOfferValidator isDescriptionEmpty() {
        return tourCreate -> tourCreate.getDescription().isEmpty() ? 
                DESCRIPTION_IS_EMPTY : SUCCESS;
    }
    
    default TourOfferValidator and (TourOfferValidator other) {
        return tourCreate -> {
            ValidationResult result = this.apply(tourCreate);
            return result.equals(SUCCESS) ? other.apply(tourCreate) : result;
        };
    }
    
    enum ValidationResult {
        SUCCESS,
        START_PLACE_IS_EMPTY,
        DESTINATION_PLACE_IS_EMPTY,
        PRICE_PER_PERSON_IS_EMPTY,
        DESCRIPTION_IS_EMPTY
    }
    
}
*/
