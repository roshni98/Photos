/**
 * @author Roshni Shah
 * */
package model;

import java.util.*;
import java.io.Serializable;

/**
 * Class modeling a Photo
 * */
public class Photo implements Serializable{

    /**
     * Setter to set a photo's tag list
     * @param tags hashmap of photo's tags to represent key-value structure
     * */
    public void setTags(HashMap<String, ArrayList<String>> tags) {
        this.tags = tags;
    }

    /**
     * Map of photo's kry-value tag pairs
     * */
    HashMap<String, ArrayList<String>> tags;

    /**
     * Photo caption
     * */
    String caption;

    /**
     * Date of capture
     * */
    Date date;

    /**
     * Photo directory path
     * */
    String path;

    /**
     * Constructor taking caption and path
     * @param c caption
     * @param path full path of photo
     * */
    public Photo(String c, String path){
        this.caption = c;
        this.date = generateDate();
        this.path = path;
        this.tags = new HashMap<>();
    }

    /**
     * Constructor taking caption, path, date, and tag map
     * @param c caption
     * @param p path
     * @param d date
     * @param t tag map
     * */
    public Photo(String c, String p, Date d, HashMap t){
        caption = c;
        date = d;
    }

    /**
     * Getter for path of photo
     * @return full path
     * */
    public String getPath(){
        return this.path;
    }

    /**
     * Getter for caption
     * @return photo caption
     * */
    public String getCaption(){
        return caption;
    }

    /**
     * Setter for caption
     * @param a new caption string
     * */
    public void setCaption(String a) {
        this.caption = a;
    }

    /**
     * Getter for photo's date of capture
     * @return photo's date of capture
     * */
    public Date getDate() {
        return this.date;
    }

    /**
     * Generates Date object for date of capture
     * @return Date object representing current time for a photo's date of capture
     * */
    public Date generateDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        //cal.setTime(date);
        //int month = cal.get(Calendar.MONTH) + 1;
        //int day = cal.get(Calendar.DAY_OF_MONTH);
        //int year = cal.get(Calendar.YEAR);
        return cal.getTime();
    }

    /**
     * Setter for date
     * @param d date to set field to
     * */
    public void setDate(Date d){
        date = d;
    }

    /**
     * Getter for tag map
     * @return tag map
     * */
    public HashMap<String, ArrayList<String>> getTags(){
        return tags;
    }

}
