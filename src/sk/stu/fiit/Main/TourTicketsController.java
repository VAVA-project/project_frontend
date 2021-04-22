/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.AddTicketToCartRequest;
import sk.stu.fiit.parsers.Requests.dto.CheckoutTicketsInCartRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteTicketFromCartRequest;
import sk.stu.fiit.parsers.Requests.dto.TicketsRequest;
import sk.stu.fiit.parsers.Responses.V2.AddTicketToCartResponses.AddTicketToCartResponse;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourTicketsResponses.TourTicketsResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class TourTicketsController implements Initializable {

    private List<TourTicket> ticketsInCart;

    @FXML
    private Button btnBack;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private Button btnPlus;
    @FXML
    private Button btnMinus;
    @FXML
    private Button btnRegister;
    @FXML
    private Label lblStartDate;
    @FXML
    private Label lblStartPlace;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblGuideName;
    @FXML
    private Label lblNumberOfTickets;
    @FXML
    private TextArea taComment;
    @FXML
    private Label lblDestination;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTicket();
        this.ticketsInCart = new ArrayList<>();

        Singleton.getInstance().getTourTickets().forEach(tourTicket -> {
            System.out.println("tourTicket id = " + tourTicket.getId());
        });

    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnBack)) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/TourBuy.fxml");
        }
    }

    private void initializeTicket() {
        lblDestination.setText(Singleton.getInstance().getTourBuy().getDestinationPlace());
        lblStartDate.setText(Singleton.getInstance().getTourDate().getStartDate());
        lblStartPlace.setText(Singleton.getInstance().getTourBuy().getStartPlace());
        lblGuideName.setText(Singleton.getInstance().getTourBuy().getGuideName());
        lblPrice.setText(Singleton.getInstance().getTourBuy().getPricePerPerson());
    }

    @FXML
    private void handlePlusButton(MouseEvent event) {
        addTicketToCart();
    }

    @FXML
    private void handleMinusButton(MouseEvent event) {
        removeTicketFromCart();
    }

    @FXML
    private void handleRagisterButton(MouseEvent event) {
        checkoutTicketsInCart();
    }

    private void addTicketToCart() {

        boolean areTicketsAvailable = true;

        if (Singleton.getInstance().getTourTickets().isEmpty()
                && Singleton.getInstance().isAreAllTicketsLoaded()) {
            Alerts.fieldsValidation("All available tickets have been reserved");
            areTicketsAvailable = false;
        } else {
            getTicketsForDate();
        }

        if (areTicketsAvailable) {
            boolean isTicketAddedToCart = false;
            int indexOfTicket = 0;

            while (!isTicketAddedToCart && indexOfTicket < Singleton.getInstance().getTourTickets().size()) {
                //System.out.println("ADDING THIS TICKET TO CART = " + Singleton.getInstance().getTourTickets().get(indexOfTicket));

                isTicketAddedToCart = sendAddTicketToCartRequest(indexOfTicket);

                //System.out.println("indexOfTicket = " + indexOfTicket);
                //System.out.println("isTicketAddedToCart = " + isTicketAddedToCart);
                if (isTicketAddedToCart) {
                    this.ticketsInCart.add(new TourTicket(Singleton.getInstance().getTourTickets().get(indexOfTicket)));
                    Singleton.getInstance().getTourTickets().remove(indexOfTicket);
                    lblNumberOfTickets.setText(String.valueOf(Integer.parseInt(lblNumberOfTickets.getText()) + 1));
                    //System.out.println("TICKETS IN CART:");
                    //System.out.println("ticketsInCart = " + ticketsInCart);
                }
                indexOfTicket++;
            }
        }

    }

    private boolean sendAddTicketToCartRequest(int indexOfTicket) {
        AddTicketToCartRequest addTicketToCartRequest = new AddTicketToCartRequest(Singleton.getInstance().getTourTickets().get(indexOfTicket).getId());
        addTicketToCartRequest.accept(new XMLRequestParser());

        HttpPost request = (HttpPost) addTicketToCartRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            AddTicketToCartResponse addTicketToCartResponse = (AddTicketToCartResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.ADD_TICKET_TO_CART_RESPONSE).parse(response);

            return addTicketToCartResponse.isIsTicketAddedToCart();

        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void removeTicketFromCart() {
        if (this.ticketsInCart.isEmpty()) {
            Alerts.fieldsValidation("Your cart is already empty");
        } else {
            if (sendDeleteTicketFromCartRequest()) {
                Singleton.getInstance().getTourTickets().add(new TourTicket(this.ticketsInCart.get(0)));
                this.ticketsInCart.remove(0);
                lblNumberOfTickets.setText(String.valueOf(Integer.parseInt(lblNumberOfTickets.getText()) - 1));
            }
        }
    }

    private boolean sendDeleteTicketFromCartRequest() {
        DeleteTicketFromCartRequest deleteTicketFromCartRequest = new DeleteTicketFromCartRequest(this.ticketsInCart.get(0).getId());
        deleteTicketFromCartRequest.accept(new XMLRequestParser());

        HttpDelete request = (HttpDelete) deleteTicketFromCartRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            // Pouzitie toho isteho Response parsera ako pri AddTicketToCart, pretoze odpoved je taka ista
            // teda <Boolean> true/false </Boolean>
            AddTicketToCartResponse deleteTicketFromCartResponse = (AddTicketToCartResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.DELETE_TICKET_TO_CART_RESPONSE).parse(response);

            return deleteTicketFromCartResponse.isIsTicketAddedToCart();

        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void checkoutTicketsInCart() {
        CheckoutTicketsInCartRequest checkoutTicketsInCartRequest = new CheckoutTicketsInCartRequest(taComment.getText());
        checkoutTicketsInCartRequest.accept(new XMLRequestParser());

        HttpPost request = (HttpPost) checkoutTicketsInCartRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {
            
            
            
            
            // Pouzitie toho isteho Response parsera ako pri AddTicketToCart, pretoze odpoved je taka ista
            // teda <Boolean> true/false </Boolean>
            AddTicketToCartResponse deleteTicketFromCartResponse = (AddTicketToCartResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.ADD_TICKET_TO_CART_RESPONSE).parse(response);

        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

    }

    private void getTicketsForDate() {
        TicketsRequest ticketsRequest = new TicketsRequest(Singleton.getInstance().getTourDate().getId());
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
