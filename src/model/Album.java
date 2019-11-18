package model;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;


public class Album implements Serializable {

    String user;
    String albumName;
    ArrayList<Photo> pics;
    Date min_date;
    Date max_date;

    private boolean dateSet;

    public Album(String user, String n) {
        this.user = user;
        this.albumName = n;
        this.pics = new ArrayList<Photo>();
    }


    public String getAlbumName() {
        return albumName;

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

    public Photo getThumbnail() {
        if (this.pics.size() <= 0) {
            return null;
        } else {
            return this.pics.get(0);
        }
    }
}
