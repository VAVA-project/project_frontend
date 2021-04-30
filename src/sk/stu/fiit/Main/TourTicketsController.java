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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

    private double xOffset = 0;
    private double yOffset = 0;

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
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(TourTicketsController.class);
    
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
        LOGGER.setLevel(org.apache.log4j.Level.INFO);
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

    /**
     * Fills labels on this screen with data about a certain tour and tour
     * guide.
     */
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
                getPricePerPerson() + " €");
    }

    /**
     * Calls methods fetchAvailableTickets and processFetchedAvailableTickets.
     *
     * @param callback
     */
    private void getFreeTickets(Consumer<? super Void> callback) {
        CompletableFuture.supplyAsync(this::fetchAvailableTickets).thenAccept(
                this::processFetchedAvailableTickets).thenAccept(callback);
    }

    /**
     * Creates TicketsRequest for the selected tour date. Then sends this
     * request to the server as HttpGet and returns the response from the
     * server. Data in the response contains list of tickets for certain tour
     * date.
     *
     * @param event
     * @see TicketsRequest
     * @see TourTicketsResponse
     * @see TourTicket
     *
     * @return TourTicketsResponse
     */
    private TourTicketsResponse fetchAvailableTickets() {
        TicketsRequest ticketsRequest = new TicketsRequest(this.tourDate.getId());
        ticketsRequest.accept(new XMLRequestParser());

        HttpGet request = (HttpGet) ticketsRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            return (TourTicketsResponse) ResponseFactory.
                    getFactory(ResponseFactory.ResponseFactoryType.TOUR_TICKETS_RESPONSE).
                    parse(response);

        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }

        return null;
    }

    /**
     * Processes the response, respectively stores the tickets from the
     * response.
     *
     * @param response
     * @see TourTicket
     */
    private void processFetchedAvailableTickets(TourTicketsResponse response) {
        if (response == null) {
            return;
        }

        List<TourTicket> fetchedTickets = response.getTourTickets();

        if (fetchedTickets.isEmpty()) {
            LOGGER.info("All available tickets have been reserved");
            Alerts.showAlert("TITLE_TICKETS_RESERVED");
            this.blockPlusButton();
            return;
        }
        this.availableTickets.addAll(fetchedTickets);
    }

    /**
     * Adds ticket to the cart.
     *
     * @param event
     */
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

    /**
     * Sends request to the server and updates label with count of reserved
     * tickets.
     *
     * @param event
     */
    private void addTicketToCart(MouseEvent event) {
        CompletableFuture.supplyAsync(() -> this.sendAddTicketToCartRequest(
                this.availableTickets.iterator().next())).thenAccept(
                (result) -> {
                    TourTicket lockedTicket = this.availableTickets.iterator().
                            next();
                    this.availableTickets.remove(lockedTicket);
                    if (!result) {
                        Platform.runLater(() -> {
                            handlePlusButton(event);
                        });
                        return;
                    }
                    synchronized (this) {
                        this.ticketsInCart.add(lockedTicket);
                        updateTicketCountLabel();
                    }

                });
    }
    
    /**
     * Deletes ticket from the cart.
     * @param event 
     */
    @FXML
    private void handleMinusButton(MouseEvent event) {
        if (this.ticketsInCart.isEmpty()) {
            LOGGER.info("Cart is already empty");
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

                    TourTicket ticketToDelete = this.ticketsInCart.iterator().next();
                    this.ticketsInCart.remove(ticketToDelete);

                    updateTicketCountLabel();
                });

        this.unblockPlusButtons();
    }
    
    /**
     * Updates ticket count label.
     */
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
    
    /**
     * Sends CheckoutTicketsInCartRequest to the server and
     * processes the response from the server.
     * 
     * @param event
     * 
     * @see CheckoutTicketsInCartRequest
     * @see CheckoutTicketsInCartResponse
     */
    @FXML
    private void handleRegisterButton(MouseEvent event) {
        if (this.ticketsInCart.isEmpty()) {
            LOGGER.info("Cart is empty");
            Alerts.showAlert("TITLE_EMPTY_CART");
            return;
        }

        CheckoutTicketsInCartResponse response = this.checkoutTicketsInCart();

        if (response != null && response.isSuccess()) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                    "Views/PostCheckout.fxml");
        } else {
            LOGGER.error("Checkout was not successfull");
            Alerts.showAlert("TITLE_CHECKOUT_ERROR");
        }
    }

    /**
     * Creates AddTicketToCartRequest for one availableTicket. Then sends this
     * request to the server as HttpPost and returns the response from the
     * server. Data in the response contains boolean value.
     *
     * @param availableTicket
     * @return boolean
     *
     * @see AddTicketToCartRequest
     * @see TicketToCartResponse
     * @see TourTicket
     */
    private boolean sendAddTicketToCartRequest(TourTicket availableTicket) {
        if (availableTicket == null) {
            return false;
        }

        AddTicketToCartRequest addTicketToCartRequest = new AddTicketToCartRequest(
                availableTicket.getId());
        addTicketToCartRequest.accept(new XMLRequestParser());

        HttpPost request = (HttpPost) addTicketToCartRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            TicketToCartResponse addTicketToCartResponse = (TicketToCartResponse) ResponseFactory.
                    getFactory(ResponseFactory.ResponseFactoryType.ADD_TICKET_TO_CART_RESPONSE).
                    parse(response);

            return addTicketToCartResponse.isIsTicketAddedToCart();
        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }

        return false;
    }
    
    /**
     * Creates DeleteTicketFromCartRequest for one ticket. Then sends this
     * request to the server as HttpDelete and returns the response from the
     * server. Data in the response contains boolean value.
     * 
     * @param tourTicket
     * @return boolean
     * 
     * @see DeleteTicketFromCartRequest
     * @see TicketToCartResponse
     * @see TourTicket
     */
    private boolean sendDeleteTicketFromCartRequest(TourTicket tourTicket) {
        if (tourTicket == null) {
            return false;
        }

        DeleteTicketFromCartRequest deleteTicketFromCartRequest = new DeleteTicketFromCartRequest(
                tourTicket.getId());
        deleteTicketFromCartRequest.accept(new XMLRequestParser());

        HttpDelete request = (HttpDelete) deleteTicketFromCartRequest.
                getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            TicketToCartResponse deleteTicketFromCartResponse = (TicketToCartResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.DELETE_TICKET_TO_CART_RESPONSE).
                    parse(response);

            return deleteTicketFromCartResponse.isIsTicketAddedToCart();

        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }
        return false;
    }
    
    /**
     * Sends sendDeleteCartRequest to the server and processes
     * the response from the server.
     */
    private void clearCart() {
        CompletableFuture.supplyAsync(this::sendDeleteCartRequest)
                .thenAccept((response) -> {
                    if (!response.isSuccess()) {
                        LOGGER.info("Cart was not successfully cleared");
                        Alerts.showAlert("TITLE_CART_CLEARED");
                    }
                });
    }
    
    /**
     * Creates DeleteCartRequest. Then sends this request to the server as
     * HttpDelete and returns the response from the server. Data in the
     * response contains boolean value.
     * 
     * @return DeleteCartResponse
     * 
     * @see DeleteCartRequest
     * @see DeleteCartResponse
     */
    private DeleteCartResponse sendDeleteCartRequest() {
        DeleteCartRequest request = new DeleteCartRequest();
        request.accept(new XMLRequestParser());

        HttpDelete deleteRequest = (HttpDelete) request.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(
                        deleteRequest)) {
            return (DeleteCartResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.DELETE_CART_RESPONSE).
                    parse(response);
        } catch (IOException e) {
            LOGGER.error("Server error" + e.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }

        return null;
    }
    
    /**
     * Creates CheckoutTicketsInCartRequest for tickets in the cart. Then sends 
     * this request to the server as HttpPost and returns the response from the
     * server. Data in the response contains boolean value.
     * 
     * 
     * @return CheckoutTicketsInCartResponse
     * 
     * @see CheckoutTicketsInCartRequest
     * @see CheckoutTicketsInCartResponse
     */
    private CheckoutTicketsInCartResponse checkoutTicketsInCart() {
        CheckoutTicketsInCartRequest checkoutTicketsInCartRequest = new CheckoutTicketsInCartRequest(
                taComment.getText());
        checkoutTicketsInCartRequest.accept(new XMLRequestParser());

        HttpPost request = (HttpPost) checkoutTicketsInCartRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            return (CheckoutTicketsInCartResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.CHECKOUT_CART_RESPONSE).
                    parse(response);
        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
            handleExpiredTicketsError(ex);
        }

        return null;
    }
    
    /**
     * Handles expired tickets error.
     * @param e 
     */
    private void handleExpiredTicketsError(APIValidationException e) {
        List<APIValidationError> errors = e.getValidationErrors();

        errors.stream().forEach(error -> {
            this.ticketsInCart.removeIf(ticket -> ticket.getId().equals(
                    error.getErrorMessage()));
        });

        this.updateTicketCountLabel();
    }

    /**
     * Sets a new position of stage depending on the variables stored from
     * setOnMousePressed method when mouse is dragged.
     *
     * @param event
     * @see setOnMousePressed
     */
    @FXML
    private void setOnMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    /**
     * Saves the axis values of the scene when mouse is pressed.
     *
     * @param event
     */
    @FXML
    private void setOnMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

}
