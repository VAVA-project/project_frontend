/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.util.List;
import sk.stu.fiit.User.User;

/**
 *
 * @author adamf
 */
public class Singleton {

    private static Singleton instance = null;

    private String jwtToken;
    private User user;
    private List<Tour> tours;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    
    
}


