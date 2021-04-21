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
 *
 * @author adamf
 */
public class Singleton {

    private static Singleton instance = null;
    
    // Data o pouzivatelovi
    private String jwtToken;
    private User user;
    
    // Data pre Tours obrazovku, resp. pre zobrazovanie tur na obrazovke
    private String actualDestination;
    private int lastPageNumber;
    private int actualPageNumber;
    private List<Tour> tours;
    private List<TourGuide> tourGuides;
    private Map<Integer, ArrayList<Tour>> allPages;
    
    // Data pre TourBuy obrazovku
    private Tour tourBuy;
    private boolean areAllTourDatesLoaded;
    private int pageNumberToLoad;
    private List<TourDate> tourDates;
    

    private Singleton() {
        this.lastPageNumber = -1;
        this.tourGuides = new ArrayList<>();
        this.allPages = new HashMap<>();
    }

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
    
    // Metoda ktora skopiruje aktualnu page (tury na stranke) a vlozi ju do 
    // HashMapy uz navstivenych pages. Zabezpecenie toho, aby sa z DB 
    // nenacitavali znova uz zobrazene tury, resp. page s turami
    public void insertPageIntoAllPages(){
        ArrayList<Tour> newPage = new ArrayList<>();
        this.tours.forEach(tour -> {
            newPage.add(new Tour(tour));
        });
        this.allPages.put(this.actualPageNumber, newPage);
    }
    
}


