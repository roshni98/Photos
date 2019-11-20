package model;

import java.util.*;
import java.io.Serializable;


public class Photo implements Serializable{
    static final long serialVersionUID = 1;
    HashMap<String, ArrayList<String>> tags;
    String caption;
    Date date;
    String path;

    public Photo(String c, String path){
        this.caption = c;
        this.date = null; // TODO set date and time - use calendar class
        this.path = path;
        this.tags = new HashMap<>();
    }

    public Photo(String c, String p, Date d, HashMap t){
        caption = c;
        date = d;
    }

    public String getPath(){
        return this.path;
    }

    public String getCaption(){
        return caption;
    }

    public void setCaption(String a) {
        this.caption = a;
    }


    public boolean setlabel(String c){
        caption = c;
        return false;
    }

    public String getDate(){
        Calendar cal = Calendar.getInstance();
        if(date == null){
            return "";
        }
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        return month + "/" + day + "/" + year;

    }

    public void setDate(Date d){
        date = d;
    }

    public HashMap<String, ArrayList<String>> getTags(){
        return tags;
    }

}
