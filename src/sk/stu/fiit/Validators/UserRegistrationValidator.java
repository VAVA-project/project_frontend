/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Validators;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;

/**
 *
 * @author adamf
 */
public class UserRegistrationValidator {
    
    private static Pattern emailPattern = Pattern.compile("^[^\\s@]+@([^\\s@.,]+\\.)+[^\\s@.,]{2,}$");
    private static Pattern passwordPattern = Pattern.compile("[^\\s]{8,}");
    
    /**
     * Validates if the given text fields are empty (email, password,
     * firstname, lastname).
     */
    public static BiPredicate<TextField, TextField> areEmpty = (tf1, tf2) -> {
        return !(tf1.getText().isEmpty() || tf2.getText().isEmpty());
    };
    
    /**
     * Validates if the given email matches regex in the emailPattern.
     */
    public static Predicate<String> isEmailValid = email -> {
        return emailPattern.matcher(email).find();
    };
    
    /**
     * Validates if the given password matches regex in the passwordPattern.
     */
    public static Predicate<String> isPasswordValid = password -> {
        return passwordPattern.matcher(password).find();
    };
    
    /**
     * validates if the user is more than 15 years old.
     */
    public static Predicate<LocalDate> isDateValid = date -> {
        return Period.between(date, LocalDate.now()).getYears() >= 15;
    };

}
