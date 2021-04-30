/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.util.Objects;

/**
 * Stores data about the tour ticket.
 * @author adamf
 */
public class TourTicket {
    
    private String id;
    private String createdAt;
    private String updatedAt;
    
    /**
     * 
     * @param id attribute that stores tour ticket id
     * @param createdAt attribute that stores date in string format and tells,
     * when the ticket has been created
     * @param updatedAt attribute that stores date in string format and tells,
     * when the ticket has been updated
     */
    public TourTicket(String id, String createdAt, String updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    /**
     * Creates copy of the given tourTicket object.
     * 
     * @param tourTicket 
     */
    public TourTicket(TourTicket tourTicket) {
        this.id = tourTicket.getId();
        this.createdAt = tourTicket.getCreatedAt();
        this.updatedAt = tourTicket.getUpdatedAt();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.createdAt);
        hash = 53 * hash + Objects.hashCode(this.updatedAt);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TourTicket other = (TourTicket) obj;
        return true;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
}
