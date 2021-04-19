/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 *
 * @author adamf
 */
public class GUITourElements {
    
    public ImageView photo;
    public Label lblName;
    public Label lblDestination;
    public Label lblRating;
    public Label lblPrice;

    public GUITourElements(ImageView photo, Label lblName, Label lblDestination, Label lblRating, Label lblPrice) {
        this.photo = photo;
        this.lblName = lblName;
        this.lblDestination = lblDestination;
        this.lblRating = lblRating;
        this.lblPrice = lblPrice;
    }

}
