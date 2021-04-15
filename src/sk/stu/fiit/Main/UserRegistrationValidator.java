/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javafx.scene.control.TextField;

/**
 *
 * @author adamf
 */
public class UserRegistrationValidator {

    static BiPredicate<TextField, TextField> areEmpty = (tf1, tf2) -> {
        return !(tf1.getText().isEmpty() || tf2.getText().isEmpty());
    };

    static Predicate<String> isEmailValid = email -> {
        return email.matches("^[^\\s@]+@([^\\s@.,]+\\.)+[^\\s@.,]{2,}$");
    };

    static Predicate<String> isPasswordValid = password -> {
        return password.matches("[^\\s]{8,}");
    };

    static Predicate<LocalDate> isDateValid = date -> {
        return Period.between(date, LocalDate.now()).getYears() >= 15;
    };

}

/*
public final class UserRegistrationValidator {

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
 */

