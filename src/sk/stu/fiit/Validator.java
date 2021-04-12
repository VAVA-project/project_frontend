/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.scene.control.TextField;

/**
 *
 * @author adamf
 */
public final class Validator {

    // Validating that the given fields are not empty
    static boolean validateFieldsAreEmpty(List<TextField> textFields) {
        for (TextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    static boolean validateDateRange(String dateToValidate) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        
        try {
            Date date = sdf.parse(dateToValidate);
            
            Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.YEAR, 0);
            
            Calendar currentDateBefore150years = Calendar.getInstance();
            currentDateBefore150years.add(Calendar.YEAR, -150);
            
            if (date.before(currentDate.getTime())
                    && date.after(currentDateBefore150years.getTime())) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

}
