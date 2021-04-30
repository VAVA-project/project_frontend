/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sk.stu.fiit.User.User;

/**
 * Stores all the necessary data.
 * @author adamf
 */
public class Singleton {

    private static Singleton instance = null;
    
    // User's data
    private String jwtToken;
    private User user;
    
    // Data for the Tours screen, for displaying tours on the screen
    private String actualDestination;
    private int lastPageNumber;
    private int actualPageNumber;
    private List<Tour> tours;
    private List<TourGuide> tourGuides;
    private Map<Integer, ArrayList<Tour>> allPages;
    
    // Data for the TourBuy screen
    private Tour tourBuy;
    private boolean areAllTourDatesLoaded;
    private int pageNumberToLoad;
    private List<TourDate> tourDates;
    
    // Data for the CreateTourOffer and CreateSchedule screen
    private TourCreate tourCreate;
    private List<TourDate> tourDatesOnScreen;
    private boolean tourDateDeleted;
    
    private Singleton() {
        this.lastPageNumber = -1;
        this.tourGuides = new ArrayList<>();
        this.tours = new ArrayList<>();
        this.allPages = new HashMap<>();
        this.tourDates = new ArrayList<>();
        this.tourDatesOnScreen = new ArrayList<>();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    
    public void clearTourBuy() {
        this.tourBuy = null;
        this.areAllTourDatesLoaded = false;
        this.pageNumberToLoad = 0;
        this.tourDates.clear();
    }
    
    public void clearTours() {
        this.actualDestination = null;
        this.lastPageNumber = 0;
        this.actualPageNumber = 0;
        this.tours.clear();
        this.tourGuides.clear();
        this.allPages.clear();
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

    public List<TourGuide> getTourGuides() {
        return tourGuides;
    }

    public void addTourGuide(TourGuide tourGuide){
        this.tourGuides.add(tourGuide);
    }

    public int getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(int lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }
    
    public int getActualPageNumber() {
        return actualPageNumber;
    }

    public void setActualPageNumber(int actualPageNumber) {
        this.actualPageNumber = actualPageNumber;
    }

    public String getActualDestination() {
        return actualDestination;
    }

    public void setActualDestination(String actualDestination) {
        this.actualDestination = actualDestination;
    }

    public Map<Integer, ArrayList<Tour>> getAllPages() {
        return allPages;
    }

    public void setAllPages(Map<Integer, ArrayList<Tour>> allPages) {
        this.allPages = allPages;
    }

    public Tour getTourBuy() {
        return tourBuy;
    }

    public void setTourBuy(Tour tourBuy) {
        this.tourBuy = tourBuy;
    }

    public boolean isAreAllTourDatesLoaded() {
        return areAllTourDatesLoaded;
    }

    public void setAreAllTourDatesLoaded(boolean areAllTourDatesLoaded) {
        this.areAllTourDatesLoaded = areAllTourDatesLoaded;
    }

    public int getPageNumberToLoad() {
        return pageNumberToLoad;
    }

    public void setPageNumberToLoad(int pageNumberToLoad) {
        this.pageNumberToLoad = pageNumberToLoad;
    }

    public List<TourDate> getTourDates() {
        return tourDates;
    }

    public void setTourDates(List<TourDate> tourDates) {
        this.tourDates = tourDates;
    }

    public TourCreate getTourCreate() {
        return tourCreate;
    }

    public void setTourCreate(TourCreate tourCreate) {
        this.tourCreate = tourCreate;
    }

    public List<TourDate> getTourDatesOnScreen() {
        return tourDatesOnScreen;
    }

    public boolean isTourDateDeleted() {
        return tourDateDeleted;
    }

    public void setTourDateDeleted(boolean tourDateDeleted) {
        this.tourDateDeleted = tourDateDeleted;
    }
    
    // A method that copies the current page and inserts it into
    // the HashMap for already loaded pages. Ensuring that from the database
    // did not reload the already displayed tours, respectively page with tours
    public void insertPageIntoAllPages(){
        ArrayList<Tour> newPage = new ArrayList<>();
        this.tours.forEach(tour -> {
            newPage.add(new Tour(tour));
        });
        this.allPages.put(this.actualPageNumber, newPage);
    }
    
}


