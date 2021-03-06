/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import sk.stu.fiit.parsers.Responses.V2.AddTicketToCartResponses.TicketToCartResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.CheckoutTicketsInCartResponses.CheckoutTicketsInCartResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.DeleteCartResponses.DeleteCartResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.EditResponses.EditResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.LoginResponses.LoginResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.RatingResponses.RatingResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.RegisterResponses.RegisterResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.SearchResponses.SearchResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.CreateTourDateResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.DeleteTourDateResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.TourDatesResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourOfferResponses.DeleteTourOfferResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourOfferResponses.TourOfferResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourTicketsResponses.TourTicketsResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.UserBookingsResponses.UserBookingsResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.UserResponses.UserResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.UserToursResponses.UserToursResponseFactory;

/**
 * ResponseFactory represents main factory that is used to call particular
 * subfactories which are used to parse response
 *
 * @author Adam Bublavý
 */
public class ResponseFactory {
    
    private static final Logger LOGGER = Logger.getLogger(ResponseFactory.class);
    
    public static enum ResponseFactoryType {
        EDIT_RESPONSE,
        LOGIN_RESPONSE,
        REGISTER_RESPONSE,
        SEACH_RESPONSE,
        USER_RESPONSE,
        USER_TOURS_RESPONSE,
        TOUR_DATES_RESPONSE,
        CREATE_TOUR_OFFER_RESPONSE,
        EDIT_TOUR_OFFER_RESPONSE,
        DELETE_TOUR_OFFER_RESPONSE,
        DELETE_TOUR_DATE_RESPONSE,
        TOUR_TICKETS_RESPONSE,
        ADD_TICKET_TO_CART_RESPONSE,
        DELETE_TICKET_TO_CART_RESPONSE,
        DELETE_CART_RESPONSE,
        CHECKOUT_CART_RESPONSE,
        CREATE_TOUR_DATE_RESPONSE,
        BOOKED_TOURS_RESPONSE,
        COMPLETED_TOURS_RESPONSE,
        RATING_RESPONSE
    }
    
    private static final Map<ResponseFactoryType, Class<? extends AbstractResponseFactory>> registeredFactoryTypes = new HashMap<>();
    
    static {
        registeredFactoryTypes.put(ResponseFactoryType.EDIT_RESPONSE,
                EditResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.LOGIN_RESPONSE,
                LoginResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.REGISTER_RESPONSE,
                RegisterResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.SEACH_RESPONSE,
                SearchResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.USER_RESPONSE,
                UserResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.USER_TOURS_RESPONSE,
                UserToursResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.TOUR_DATES_RESPONSE,
                TourDatesResponseFactory.class);
        registeredFactoryTypes.put(
                ResponseFactoryType.CREATE_TOUR_OFFER_RESPONSE,
                TourOfferResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.EDIT_TOUR_OFFER_RESPONSE,
                TourOfferResponseFactory.class);
        registeredFactoryTypes.put(
                ResponseFactoryType.DELETE_TOUR_OFFER_RESPONSE,
                DeleteTourOfferResponseFactory.class);
        registeredFactoryTypes.
                put(ResponseFactoryType.DELETE_TOUR_DATE_RESPONSE,
                        DeleteTourDateResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.TOUR_TICKETS_RESPONSE,
                TourTicketsResponseFactory.class);
        registeredFactoryTypes.put(
                ResponseFactoryType.ADD_TICKET_TO_CART_RESPONSE,
                TicketToCartResponseFactory.class);
        registeredFactoryTypes.put(
                ResponseFactoryType.DELETE_TICKET_TO_CART_RESPONSE,
                TicketToCartResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.DELETE_CART_RESPONSE,
                DeleteCartResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.CHECKOUT_CART_RESPONSE,
                CheckoutTicketsInCartResponseFactory.class);
        registeredFactoryTypes.
                put(ResponseFactoryType.CREATE_TOUR_DATE_RESPONSE,
                        CreateTourDateResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.BOOKED_TOURS_RESPONSE,
                UserBookingsResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.COMPLETED_TOURS_RESPONSE,
                UserBookingsResponseFactory.class);
        registeredFactoryTypes.put(ResponseFactoryType.RATING_RESPONSE,
                RatingResponseFactory.class);
    }

    /**
     * Creates new instance of given factory
     *
     * @param type Factory type that will be created
     * @return Returns newly created factory
     *
     * @see AbstractResponseFactory
     */
    public static AbstractResponseFactory getFactory(ResponseFactoryType type) {
        LOGGER.info("Creating new " + type.toString() + " factory");
        
        Class<?> registeredClass = registeredFactoryTypes.get(type);
        
        try {
            return (AbstractResponseFactory) registeredClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            try {
                return (AbstractResponseFactory) registeredClass.
                        getDeclaredMethod("getInstance", (Class<?>[]) null).
                        invoke(registeredClass);
            } catch (NoSuchMethodException | SecurityException
                    | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException ex1) {
                LOGGER.error(
                        "Exception has been thrown when creating new instance of " + type.
                                toString() + " factory. Default constructor or getInstance method was not found");
            }
        }
        
        return null;
    }
    
}
