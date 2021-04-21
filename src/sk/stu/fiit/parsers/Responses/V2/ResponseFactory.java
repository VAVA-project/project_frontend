/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.stu.fiit.parsers.Responses.V2.EditResponses.EditResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.LoginResponses.LoginResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.RegisterResponses.RegisterResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.SearchResponses.SearchResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.TourDatesResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourTicketsResponses.TourTicketsResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.UserResponses.UserResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.UserToursResponses.UserToursResponseFactory;

/**
 *
 * @author Adam Bublavý
 */
public class ResponseFactory {

    public static enum ResponseFactoryType {
        EDIT_RESPONSE,
        LOGIN_RESPONSE,
        REGISTER_RESPONSE,
        SEACH_RESPONSE,
        USER_RESPONSE,
        USER_TOURS_RESPONSE,
        TOUR_DATES_RESPONSE,
        TOUR_TICKETS_RESPONSE
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
        registeredFactoryTypes.put(ResponseFactoryType.TOUR_TICKETS_RESPONSE, 
                TourTicketsResponseFactory.class);
    }

    public static AbstractResponseFactory getFactory(ResponseFactoryType type) {
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
                Logger.getLogger(ResponseFactory.class.getName()).
                        log(Level.SEVERE, null, ex1);
            }
        }

        return null;
    }

}
