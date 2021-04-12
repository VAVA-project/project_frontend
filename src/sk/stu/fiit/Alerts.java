/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit;

import javafx.scene.control.Alert;

/**
 *
 * @author adamf
 */
public class Alerts {

    static void fieldsValidation(String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Fields validation");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    static void photoChoosing() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Photo choosing");
        alert.setHeaderText(null);
        alert.setContentText("File not found");
        alert.showAndWait();
    }
}
