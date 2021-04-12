/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit;

/**
 *
 * @author adamf
 */
public class Singleton {

    private static Singleton instance = null;

    private String jwtToken;
    private User user;

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
    
}


