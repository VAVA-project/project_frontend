/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Validators;

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
    
    public static BiPredicate<TextField, TextField> areEmpty = (tf1, tf2) -> {
        return !(tf1.getText().isEmpty() || tf2.getText().isEmpty());
    };

    public static Predicate<String> isEmailValid = email -> {
        return email.matches("^[^\\s@]+@([^\\s@.,]+\\.)+[^\\s@.,]{2,}$");
    };

    public static Predicate<String> isPasswordValid = password -> {
        return password.matches("[^\\s]{8,}");
    };

    public static Predicate<LocalDate> isDateValid = date -> {
        return Period.between(date, LocalDate.now()).getYears() >= 15;
    };

}
