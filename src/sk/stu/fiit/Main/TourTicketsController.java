/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
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

    private Set<TourTicket> ticketsInCart;
    private Set<TourTicket> availableTickets;

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
    @FXML
    private Label lblEndDate;

    public TourTicketsController() {
        this.ticketsInCart = ConcurrentHashMap.newKeySet();
        this.availableTickets = ConcurrentHashMap.newKeySet();
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
        String date = this.tourDate.getStartDate();
        String[] parts = date.split(", ");
        String part1 = parts[0];
        String part2 = parts[1];
        lblStartDate.setText(part1 + "\n" + part2);
        date = this.tourDate.getEndDate();
        parts = date.split(", ");
        part1 = parts[0];
        part2 = parts[1];
        lblEndDate.setText(part1 + "\n" + part2);
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
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(
                    Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
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
            Alerts.showAlert("TITLE_TICKETS_RESERVED");
            this.blockPlusButton();
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
            
            this.unblockMinusButtons();

            return;
        }
        
        this.unblockMinusButtons();

        addTicketToCart(event);
    }

    private void addTicketToCart(MouseEvent event) {
        CompletableFuture.supplyAsync(() -> this.sendAddTicketToCartRequest(
                this.availableTickets.iterator().next())).thenAccept(
                (result) -> {
                    if (!result) {
                        return;
                    }

                    System.out.println(
                            "Available tickets size: " + this.availableTickets.
                                    size());

                    TourTicket lockedTicket = this.availableTickets.iterator().
                            next();
                    this.availableTickets.remove(lockedTicket);
                    this.ticketsInCart.add(lockedTicket);

                    updateTicketCountLabel();
                });
    }

    @FXML
    private void handleMinusButton(MouseEvent event) {
        if (this.ticketsInCart.isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_CART_ALREADY");
            this.blockMinusButtons();
            return;
        }

        CompletableFuture.supplyAsync(() -> this.
                sendDeleteTicketFromCartRequest(this.ticketsInCart.iterator().
                        next())).
                thenAccept((response) -> {
                    if (!response) {
                        return;
                    }

                    TourTicket ticketToDelete = this.ticketsInCart.iterator().
                            next();
                    this.ticketsInCart.remove(ticketToDelete);

                    updateTicketCountLabel();
                });
        
        this.unblockPlusButtons();
    }

    private void updateTicketCountLabel() {
        Platform.runLater(() -> this.lblNumberOfTickets.setText(
                String.valueOf(this.ticketsInCart.size())));
    }

    private void blockPlusButton() {
        Platform.runLater(() -> {
            this.btnPlus.setDisable(true);
        });
    }

    private void blockMinusButtons() {
        Platform.runLater(() -> {
            this.btnMinus.setDisable(true);
        });
    }

    private void unblockPlusButtons() {
        Platform.runLater(() -> {
            this.btnPlus.setDisable(false);
        });
    }

    private void unblockMinusButtons() {
        Platform.runLater(() -> {
            this.btnMinus.setDisable(false);
        });
    }

    @FXML
    private void handleRagisterButton(MouseEvent event) {
        if (this.ticketsInCart.isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_CART");
            return;
        }

        if (checkoutTicketsInCart().isSuccess()) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                    "Views/PostCheckout.fxml");
        } else {
            Alerts.showAlert("TITLE_CHECKOUT_ERROR");
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
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
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
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
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
                        Alerts.showAlert("TITLE_CART_CLEARED");
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
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
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
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
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
