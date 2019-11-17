package model;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;


public class Album implements Serializable {

    String name;
    ArrayList<Photo> pics;

    Date max_date;

    Date min_date;

    private boolean dateSet;


    public Album(String n) {
        name = n;
        pics = new ArrayList<Photo>();
        max_date = new Date(Long.MIN_VALUE);
        min_date = new Date(Long.MAX_VALUE);
        dateSet = false;
    }


    public String getName() {
        return name;
    }

    public void setName(String n) {
        //make sure no other album has same name
        name = n;
    }

    public boolean addPic(Photo p) {
        pics.add(p);
        if (!dateSet) {
            max_date = p.date;
            min_date = p.date;
            dateSet = true;
        } else {
            if (max_date.before(p.date)) {
                max_date = p.date;
            } else if (min_date.after(p.date)) {
                min_date = p.date;
            }
        }
        return false;
    }

    public void removePic(Photo p) {
        pics.remove(p);
    }


    public ArrayList<Photo> getPics() {
        return pics;
    }


    public String toString() {
        return getName();
    }


    public Photo getThumbnail() {
        if (this.pics.size() <= 0) {
            return null;
        } else {
            return this.pics.get(0);
        }
    }
}
