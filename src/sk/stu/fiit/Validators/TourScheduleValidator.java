/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sk.stu.fiit.Main.Alerts;

/**
 *
 * @author adamf
 */
public class TourScheduleValidator {
    
    private static Pattern timePattern = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(TourScheduleValidator.class);
    
    /**
     * Validates the given text fields if they are empty, if date pickers
     * have the value set, if capacity is of the Integer data type, if 
     * times matches regex in the timePattern (HH:MM) and whether 
     * the end date is not less than the start date.
     * 
     * @param tfCapacity
     * @param dpStartDate
     * @param dpEndDate
     * @param tfStartTime
     * @param tfEndTime
     * @return 
     */
    public static boolean validate(TextField tfCapacity, DatePicker dpStartDate, 
            DatePicker dpEndDate, TextField tfStartTime, TextField tfEndTime) {
        if (tfCapacity.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_CAPACITY");
            return false;
        }
        if (dpStartDate.getValue() == null) {
            Alerts.showAlert("TITLE_EMPTY_START_DATE");
            return false;
        }
        if (dpEndDate.getValue() == null) {
            Alerts.showAlert("TITLE_EMPTY_END_DATE");
            return false;
        }
        if (tfStartTime.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_START_TIME");
            return false;
        }
        if (tfEndTime.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_END_TIME");
            return false;
        }
        if (isInteger(tfCapacity)) {
            Alerts.showAlert("TITLE_CAPACITY_NOT_NUMBER");
            return false;
        }
        if (!timePattern.matcher(tfStartTime.getText()).find()) {
            Alerts.showAlert("TITLE_START_TIME");
            return false;
        }
        if (!timePattern.matcher(tfEndTime.getText()).find()) {
            Alerts.showAlert("TITLE_END_TIME");
            return false;
        }
        if (validateDateRange(dpStartDate, dpEndDate)) {
            Alerts.showAlert("TITLE_DATE_RANGE");
            return false;
        }
        return true;
    }
    
    /**
     * Validates whether the end date is not less than the start date.
     * 
     * @param dpStartDate
     * @param dpEndDate
     * @return boolean
     */
    static boolean validateDateRange(DatePicker dpStartDate, DatePicker dpEndDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (countDaysBetweenDates(sdf.parse(dpStartDate.getValue().toString()),
                    sdf.parse(dpEndDate.getValue().toString())) >= 0) {
                return false;
            }
        } catch (ParseException ex) {
            LOGGER.error("Parse exception has raised while parsing tour date" + ex.getMessage());
        }
        return true;
    }
    
    /**
     * Counts days between two given dates.
     * 
     * @param d1
     * @param d2
     * @return Integer
     */
    static int countDaysBetweenDates(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    
    /**
     * Validates if the capacity is of the Integer data type.
     * 
     * @param tfCapacity
     * @return boolean
     */
    public static boolean isInteger(TextField tfCapacity) {
        try {
            Integer.parseInt(tfCapacity.getText());
            return false;
        } catch (NumberFormatException e) {
            LOGGER.error("Number format exception has raise while trying "
                    + "to format input in capacity text field" + e.getMessage());
            return true;
        }
    }
    
}
