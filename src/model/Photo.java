package model;

import java.util.*;
import java.io.Serializable;


public class Photo implements Serializable{
    HashMap<String, ArrayList<String>> tags;
    String caption;
    Date date;

    public Photo(String c, Date d){
        caption = c;
        date = d;
    }

    public Photo(String c, String p, Date d, HashMap t){
        caption = c;
        date = d;
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
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        return month + "/" + day + "/" + year;

    }

    public void setDate(Date d){
        date = d;
    }

}
