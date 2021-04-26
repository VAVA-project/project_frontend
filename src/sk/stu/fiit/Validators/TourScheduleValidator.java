/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sk.stu.fiit.Main.Alerts;

/**
 *
 * @author adamf
 */
public class TourScheduleValidator {
    
    public static boolean validate(TextField tfCapacity, DatePicker dpStartDate, 
            DatePicker dpEndDate, TextField tfStartTime, TextField tfEndTime) {
        
        if (tfCapacity.getText().isEmpty()) {
            Alerts.showAlert(Alerts.TITLE_EMPTY_CAPACITY);
            return false;
        }
        if (dpStartDate.getValue() == null) {
            Alerts.showAlert(Alerts.TITLE_EMPTY_START_DATE);
            return false;
        }
        if (dpEndDate.getValue() == null) {
            Alerts.showAlert(Alerts.TITLE_EMPTY_END_DATE);
            return false;
        }
        if (tfStartTime.getText().isEmpty()) {
            Alerts.showAlert(Alerts.TITLE_EMPTY_START_TIME);
            return false;
        }
        if (tfEndTime.getText().isEmpty()) {
            Alerts.showAlert(Alerts.TITLE_EMPTY_END_TIME);
            return false;
        }
        if (isInteger(tfCapacity)) {
            Alerts.showAlert(Alerts.TITLE_CAPACITY_NOT_NUMBER);
            return false;
        }
        if (!tfStartTime.getText().matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            Alerts.showAlert(Alerts.TITLE_START_TIME);
            return false;
        }
        if (!tfEndTime.getText().matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            Alerts.showAlert(Alerts.TITLE_END_TIME);
            return false;
        }
        if (validateDateRange(dpStartDate, dpEndDate)) {
            Alerts.showAlert(Alerts.TITLE_DATE_RANGE);
            return false;
        }
        return true;
    }
    
    static boolean validateDateRange(DatePicker dpStartDate, DatePicker dpEndDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (countDaysBetweenDates(sdf.parse(dpStartDate.getValue().toString()),
                    sdf.parse(dpEndDate.getValue().toString())) >= 0) {
                return false;
            }
        } catch (ParseException ex) {
            Logger.getLogger(TourScheduleValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    static int countDaysBetweenDates(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    
    public static boolean isInteger(TextField tfCapacity) {
        try {
            Integer.parseInt(tfCapacity.getText());
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
    
}
