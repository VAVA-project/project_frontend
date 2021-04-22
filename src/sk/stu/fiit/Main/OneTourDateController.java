/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.TicketsRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourTicketsResponses.TourTicketsResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class OneTourDateController implements Initializable {

    private TourDate tourDate;

    @FXML
    private Label lblCapacity;
    @FXML
    private Label lblDate;
    @FXML
    private Button btnBuy;

    public OneTourDateController() {
    }

    public OneTourDateController(TourDate tourDate) {
        this.tourDate = tourDate;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setElements();
    }

    @FXML
    private void handleBuyButton(MouseEvent event) {
        Singleton.getInstance().setTourDate(this.tourDate);
        getTicketsForDate();
        ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/TourTickets.fxml");
    }

    private void setElements() {
        this.lblCapacity.setText("0/0");
        this.lblDate.setText(tourDate.getStartDate());
    }

    private void getTicketsForDate() {
        TicketsRequest ticketsRequest = new TicketsRequest(this.tourDate.getId());
        ticketsRequest.accept(new XMLRequestParser());

        HttpGet request = (HttpGet) ticketsRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            // Ulozenie si prave nacitanych listkov, pre ich rezervovanie
            TourTicketsResponse tourTicketsResponse = (TourTicketsResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.TOUR_TICKETS_RESPONSE).parse(response);
            Singleton.getInstance().setTourTickets(tourTicketsResponse.getTourTickets());
            
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
