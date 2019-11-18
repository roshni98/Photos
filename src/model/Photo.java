package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.io.Serializable;


public class Photo implements Serializable{
    ArrayList<String> tags;
    String caption;
    Date date;
    String picPath;
    private String location = "";

    public Photo(String c, String p, Date d){
        caption = c;
        picPath = p;
        date = d;
    }

    public Photo(String c, String p, Date d, HashMap t){
        caption = c;
        picPath = p;
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


    public void setPath(String p){
        picPath = p;
    }


    public String getPath(){
        return picPath;
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
    public void setPhotoLocation(String location) {
        this.location = location;
    }

    public String getLocation() {

        return location;
    }


    public String toString(){
        return picPath;
    }


}
