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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import sk.stu.fiit.Exceptions.APIValidationError;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.AddTicketToCartRequest;
import sk.stu.fiit.parsers.Requests.dto.CheckoutTicketsInCartRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteCartRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteTicketFromCartRequest;
import sk.stu.fiit.parsers.Requests.dto.TicketsRequest;
import sk.stu.fiit.parsers.Responses.V2.AddTicketToCartResponses.TicketToCartResponse;
import sk.stu.fiit.parsers.Responses.V2.CheckoutTicketsInCartResponses.CheckoutTicketsInCartResponse;
import sk.stu.fiit.parsers.Responses.V2.DeleteCartResponses.DeleteCartResponse;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourTicketsResponses.TourTicketsResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class TourTicketsController implements Initializable {
    
    private TourDate tourDate;
    
    private List<TourTicket> ticketsInCart;
    private CopyOnWriteArrayList<TourTicket> availableTickets;
    
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
    
    public TourTicketsController() {
        this.ticketsInCart = new ArrayList<>();
        this.availableTickets = new CopyOnWriteArrayList<>();
    }
    
    public TourTicketsController(TourDate tourDate) {
        this();
        this.tourDate = tourDate;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (this.tourDate == null) {
            return;
        }
        
        this.fillLabelsWithData();
        this.getFreeTickets((res) -> {
        });
    }
    
    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().
                    getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnBack)) {
            clearCart();
            ScreenSwitcher.getScreenSwitcher().
                    switchToScreen((MouseEvent) event, "Views/TourBuy.fxml");
        }
    }
    
    private void fillLabelsWithData() {
        lblDestination.setText(Singleton.getInstance().getTourBuy().
                getDestinationPlace());
        lblStartDate.setText(this.tourDate.getStartDate());
        lblStartPlace.setText(Singleton.getInstance().getTourBuy().
                getStartPlace());
        lblGuideName.
                setText(Singleton.getInstance().getTourBuy().getGuideName());
        lblPrice.setText(Singleton.getInstance().getTourBuy().
                getPricePerPerson());
    }
    
    private void getFreeTickets(Consumer<? super Void> callback) {
        CompletableFuture.supplyAsync(this::fetchAvailableTickets).thenAccept(
                this::processFetchedAvailableTickets).thenAccept(callback);
    }
    
    private TourTicketsResponse fetchAvailableTickets() {
        TicketsRequest ticketsRequest = new TicketsRequest(this.tourDate.getId());
        ticketsRequest.accept(new XMLRequestParser());
        
        HttpGet request = (HttpGet) ticketsRequest.getRequest();
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
            
            return (TourTicketsResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.TOUR_TICKETS_RESPONSE).
                    parse(response);
            
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE,
                    null, ex);
            Alerts.serverIsNotResponding();
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(
                    Level.SEVERE, null, ex);
            Alerts.authTokenExpired();
        } catch (APIValidationException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private void processFetchedAvailableTickets(TourTicketsResponse response) {
        if (response == null) {
            return;
        }
        
        List<TourTicket> fetchedTickets = response.getTourTickets();
        
        if (fetchedTickets.isEmpty()) {
            Alerts.showGenericAlertError("Ticket reservation", null,
                    "All available tickets have been reserved");
            return;
        }
        
        this.availableTickets.addAll(fetchedTickets);
    }
    
    @FXML
    private void handlePlusButton(MouseEvent event) {
        if (this.availableTickets.isEmpty()) {
            this.getFreeTickets((response) -> {
                addTicketToCart(event);
            });
            
            return;
        }
        
        addTicketToCart(event);
    }
    
    private void addTicketToCart(MouseEvent event) {
        CompletableFuture.supplyAsync(() -> this.sendAddTicketToCartRequest(
                this.availableTickets.get(0))).thenAccept((result) -> {
            if (!result) {
                this.availableTickets.remove(0);
                handlePlusButton(event);
                return;
            }
            
            TourTicket lockedTicket = this.availableTickets.remove(0);
            this.ticketsInCart.add(lockedTicket);
            
            updateTicketCountLabel();
        });
    }
    
    @FXML
    private void handleMinusButton(MouseEvent event) {
        if (this.ticketsInCart.isEmpty()) {
            Alerts.showGenericAlertError("Ticket reservation", null,
                    "Your cart is already empty");
            return;
        }
        
        CompletableFuture.supplyAsync(() -> this.
                sendDeleteTicketFromCartRequest(this.ticketsInCart.get(0))).
                thenAccept((response) -> {
                    this.ticketsInCart.remove(0);
                    updateTicketCountLabel();
                });
    }
    
    private void updateTicketCountLabel() {
        Platform.runLater(() -> this.lblNumberOfTickets.setText(
                String.valueOf(this.ticketsInCart.size())));
    }
    
    @FXML
    private void handleRagisterButton(MouseEvent event) {
        if(this.ticketsInCart.isEmpty()) {
            Alerts.showGenericAlertError("User order", null, "Your cart is empty");
            return;
        }
        
        if (checkoutTicketsInCart().isSuccess()) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/PostCheckout.fxml");
        } else {
            Alerts.showGenericAlertError("User oder", "Fatal error",
                    "Checkout was not successfull");
        }
    }
    
    private boolean sendAddTicketToCartRequest(TourTicket availableTicket) {
        if (availableTicket == null) {
            return false;
        }
        
        AddTicketToCartRequest addTicketToCartRequest = new AddTicketToCartRequest(
                availableTicket.getId());
        addTicketToCartRequest.accept(new XMLRequestParser());
        
        HttpPost request = (HttpPost) addTicketToCartRequest.getRequest();
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
            
            TicketToCartResponse addTicketToCartResponse = (TicketToCartResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.ADD_TICKET_TO_CART_RESPONSE).
                    parse(response);
            
            return addTicketToCartResponse.isIsTicketAddedToCart();
        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(
                    Level.SEVERE, null, ex);
            Alerts.serverIsNotResponding();
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.authTokenExpired();
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    private boolean sendDeleteTicketFromCartRequest(TourTicket tourTicket) {
        if (tourTicket == null) {
            return false;
        }
        
        DeleteTicketFromCartRequest deleteTicketFromCartRequest = new DeleteTicketFromCartRequest(
                tourTicket.getId());
        deleteTicketFromCartRequest.accept(new XMLRequestParser());
        
        HttpDelete request = (HttpDelete) deleteTicketFromCartRequest.
                getRequest();
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {

            TicketToCartResponse deleteTicketFromCartResponse = (TicketToCartResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.DELETE_TICKET_TO_CART_RESPONSE).
                    parse(response);
            
            return deleteTicketFromCartResponse.isIsTicketAddedToCart();
            
        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(
                    Level.SEVERE, null, ex);
            Alerts.serverIsNotResponding();
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.authTokenExpired();
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private void clearCart() {
        CompletableFuture.supplyAsync(this::sendDeleteCartRequest)
                .thenAccept((response) -> {
                    if (!response.isSuccess()) {
                        Alerts.
                                showGenericAlertError("Clear cart",
                                        "Fatal error",
                                        "Your cart was not successfully cleared");
                    }
                });
    }
    
    private DeleteCartResponse sendDeleteCartRequest() {
        DeleteCartRequest request = new DeleteCartRequest();
        request.accept(new XMLRequestParser());
        
        HttpDelete deleteRequest = (HttpDelete) request.getRequest();
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(
                        deleteRequest)) {
            
            return (DeleteCartResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.DELETE_CART_RESPONSE).
                    parse(response);
        } catch (IOException e) {
            Logger.getLogger(TourTicketsController.class.getName()).log(
                    Level.SEVERE, null, e);
            Alerts.serverIsNotResponding();
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.authTokenExpired();
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private CheckoutTicketsInCartResponse checkoutTicketsInCart() {
        CheckoutTicketsInCartRequest checkoutTicketsInCartRequest = new CheckoutTicketsInCartRequest(
                taComment.getText());
        checkoutTicketsInCartRequest.accept(new XMLRequestParser());
        
        HttpPost request = (HttpPost) checkoutTicketsInCartRequest.getRequest();
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
            
            return (CheckoutTicketsInCartResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.CHECKOUT_CART_RESPONSE).
                    parse(response);
        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(
                    Level.SEVERE, null, ex);
            Alerts.serverIsNotResponding();
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.authTokenExpired();
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            handleExpiredTicketsError(ex);
        }
        
        return null;
    }
    
    private void handleExpiredTicketsError(APIValidationException e) {
        List<APIValidationError> errors = e.getValidationErrors();
        
        errors.stream().forEach(error -> {
            this.ticketsInCart.removeIf(ticket -> ticket.getId().equals(
                    error.getErrorMessage()));
        });
        
        this.updateTicketCountLabel();
    }
}
