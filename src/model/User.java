/**
 * @author Roshni Shah
 * */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class modeling a user
 * */
public class User implements Serializable {

    /**
     * Username for logging in
     * */
    String username;

    /**
     * List of user's albums
     * */
    List<Album> albumList;

    /**
     * Variable representing whether they are the admin
     * */
    boolean admin;

    /**
     * Constructor taking username
     * @param username user's username
     * */
    public User(String username){
        this.username = username;
        this.albumList = new ArrayList<>();
        this.admin = false;
    }

    /**
     * Getter for user's album list
     * @return List of user's albums
     * */
    public List<Album> getAlbumList() {
        return albumList;
    }

    /**
     * Setter for user's album list
     * @param albumList new album list
     * */
    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    /**
     * Getter for user's username
     * @return username
     * */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for user's username
     * @param username new username
     * */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter for boolean isAdmin
     * @param isAdmin whether user is admin
     * */
    public void setAdmin(boolean isAdmin){
        this.admin = isAdmin;
    }

    /**
     * Getter for isAdmin
     * @return whether user is the admin
     * */
    public boolean isAdmin(){
        return this.admin;
    }
}
