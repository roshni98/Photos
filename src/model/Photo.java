package model;

import java.util.*;
import java.io.Serializable;


public class Photo implements Serializable{
    public void setTags(HashMap<String, ArrayList<String>> tags) {
        this.tags = tags;
    }

    HashMap<String, ArrayList<String>> tags;
    String caption;
    Date date;
    String path;

    public Photo(String c, String path){
        this.caption = c;
        this.date = generateDate();
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

    public Date getDate() {
        return this.date;
    }

    public Date generateDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        //cal.setTime(date);
        //int month = cal.get(Calendar.MONTH) + 1;
        //int day = cal.get(Calendar.DAY_OF_MONTH);
        //int year = cal.get(Calendar.YEAR);
        return cal.getTime();
    }

    public void setDate(Date d){
        date = d;
    }

    public HashMap<String, ArrayList<String>> getTags(){
        return tags;
    }

}
