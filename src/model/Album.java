/**
 * @author Roshni Shah
 * */
package model;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Class representing an album
 * */
public class Album implements Serializable {

    /**
     * username of user who owns this album
     * */
    String user;

    /**
     * Album name
     * */
    String albumName;

    /**
     * List of Photo objects that make up album
     * */
    ArrayList<Photo> pics;

    /**
     * Setter for album min date
     * @param min_date date to set min date to
     * */
    public void setMin_date(Date min_date) {
        this.min_date = min_date;
    }

    /**
     * Setter for album max date
     * @param max_date date to set min date to
     * */
    public void setMax_date(Date max_date) {
        this.max_date = max_date;
    }

    /**
     * min date in date range of photos in album
     * */
    Date min_date;

    /**
     * max date in date range of photos in album
     * */
    Date max_date;

    /**
     * Whether date range was set for album
     * */
    private boolean dateSet;

    /**
     * Constructor
     * @param user user that owns this album
     * @param n name of album
     * */
    public Album(String user, String n) {
        this.user = user;
        this.albumName = n;
        this.pics = new ArrayList<>();
        this.min_date = new Date(Long.MIN_VALUE);
        this.max_date = new Date(Long.MIN_VALUE);
    }

    /**
     * Getter for album name
     * @return album name
     * */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Returns string representation of max date in album's date range
     * @return max date in album's date range
     * */
    public String maxDateToString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return df.format(this.max_date);
    }

    /**
     * Returns string representation of min date in album's date range
     * @return min date in album's date range
     * */
    public String minDateToString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return df.format(this.min_date);
    }

    /**
     * Sets name of album
     * @param n new name of album
     * */
    public void setName(String n) {
        //make sure no other album has same name
        this.albumName = n;
    }

    /**
     * Getter for album's photo list
     * @return list of photos in album
     * */
    public ArrayList<Photo> getPics() {
        return pics;
    }

    /**
     * Returns name of album
     * @return name of album
     * */
    public String toString() {
        return getAlbumName();
    }
}
