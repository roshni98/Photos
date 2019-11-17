package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.io.Serializable;
import java.text.SimpleDateFormat;


public class Photo implements Serializable{
    String label;
    Date date;
    String picPath;

    public Photo(String c, String p, Date d){
        label = c;
        picPath = p;
        date = d;
    }

    public Photo(String c, String p, Date d, HashMap t){
        label = c;
        picPath = p;
        date = d;
    }


    public String getCaption(){
        return label;
    }


    public boolean setlabel(String c){
        label = c;
        return false;
    }


    public void setPath(String p){
        picPath = p;
    }


    public String getPath(){
        return picPath;
    }


    public Date getDate(){
        return date;
    }


    public void setDate(Date d){
        date = d;
    }

    public String toString(){
        return picPath;
    }



}
