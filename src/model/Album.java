package model;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;


public class Album implements Serializable {
    static final long serialVersionUID = 1;
    String user;
    String albumName;
    ArrayList<Photo> pics;

    public Date getMin_date() {
        return min_date;
    }

    public void setMin_date(Date min_date) {
        this.min_date = min_date;
    }

    public Date getMax_date() {
        return max_date;
    }

    public void setMax_date(Date max_date) {
        this.max_date = max_date;
    }

    Date min_date;
    Date max_date;

    private boolean dateSet;

    public Album(String user, String n) {
        this.user = user;
        this.albumName = n;
        this.pics = new ArrayList<>();
        this.min_date = new Date(Long.MIN_VALUE);
        this.max_date = new Date(Long.MIN_VALUE);
    }

    public String getAlbumName() {
        return albumName;
    }

    public Date getMaxDate() {
        return this.max_date;
    }

    public Date getMinDate() {
        return this.min_date;
    }

    public String maxDateToString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return df.format(this.max_date);
    }
    public String minDateToString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return df.format(this.min_date);
    }

    public void setName(String n) {
        //make sure no other album has same name
        this.albumName = n;
    }

    public void addPic(Photo p) {
        pics.add(p);
        if (!dateSet) {
            this.max_date = p.date;
            this.min_date = p.date;
            this.dateSet = true;
        } else {
            if (this.max_date.before(p.date)) {
                this.max_date = p.date;
            } else if (this.min_date.after(p.date)) {
                this.min_date = p.date;
            }
        }
    }

    public void removePic(Photo p) {
        pics.remove(p);
    }

    public ArrayList<Photo> getPics() {
        return pics;
    }

    public String toString() {
        return getAlbumName();
    }

    /*public Photo getThumbnail() {
        if (this.pics.size() <= 0) {
            return null;
        } else {
            return this.pics.get(0);
        }
    }*/
}
