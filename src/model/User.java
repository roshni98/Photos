package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    String username;
    List<Album> albumList;
    boolean admin;

    public User(String username){
        this.username = username;
        this.albumList = new ArrayList<>();
        this.admin = false;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAdmin(boolean isAdmin){
        this.admin = isAdmin;
    }

    public boolean isAdmin(){
        return this.admin;
    }
}
