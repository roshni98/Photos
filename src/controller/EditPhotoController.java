/**
 * @author Roshni Shah
 * */
package controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;

import javafx.scene.image.ImageView;
import model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * @class EditPhotoController Allows user to view one photo at a time and scroll through an album
 * */
public class EditPhotoController {

    /**
     * Changes view to previous photo in album
     * */
    @FXML
    Button previousButton;

    /**
     * Changes view to next photo in album
     * */
    @FXML
    Button nextButton;

    /**
     * Allows user to logout
     * */
    @FXML
    Button logoutButton;

    /**
     * Changes view to album view
     * */
    @FXML
    Button albumListButton;

    /**
     * Label for photo tags
     * */
    @FXML
    Text tagText;

    /**
     * Label for photo date captured
     * */
    @FXML
    Text dateText;

    /**
     * Label for photo caption
     * */
    @FXML
    Text captionText;

    /**
     * Main UI panel
     * */
    @FXML
    AnchorPane rootPane;

    /**
     * ImageView representing photo
     * */
    @FXML
    private ImageView imgView;

    /**
     * List view of photo tags
     * */
    @FXML
    ListView<String> tagList;

    /**
     * List of photos in current album
     * */
    private List<Photo> photoList;

    /**
     * Index of current photo in album
     * */
    private int selectedIndex;

    /**
     * Current user
     * */
    private User u;

    /**
     * Name of current album
     * */
    private String albumName;

//need
//list of photos in album
// photo clicked on by index

    /**
     * Method to initialize controller items
     * @param pics list of photos in the same album as current photo
     * @param u current user
     * @param albumName name of current album
     * @param index index of current photo in album
     * */
    public void init(ArrayList<Photo> pics, User u, String albumName, int index) {
        this.photoList = pics;
        this.u = u;
        this.albumName = albumName;
        this.selectedIndex = index;
        display();
    }

    /**
     * Returns Image object of a photo given its path
     * @param urlp absolute path of current photo
     * @return Image object created from parameter
     * */
    private Image getAbsolutePath(String urlp){
        File directory = new File(urlp);
        String path = directory.getAbsolutePath();
        File f = new File(path);
        if(f.exists()) {
            return new Image("file:"+f.getAbsolutePath());
        }
        return null;
    }

    /**
     * Changes view to previous photo in album
     * */
    @FXML
    private void handlePrevButton(){
        if(selectedIndex == 0){
            selectedIndex = photoList.size()-1;
        }
        else if(selectedIndex > 0  && selectedIndex < this.photoList.size()){
            selectedIndex--;
        }
        display();
    }

    /**
     * Changes view to next photo in album
     * */
    @FXML
    private void handleNextButton(){
        if(selectedIndex >= 0  && selectedIndex < this.photoList.size()-1){
            selectedIndex++;
        }
        else if(selectedIndex == photoList.size() - 1){
            selectedIndex = 0;
        }
        display();
    }

    /**
     * Allows user to logout
     * */
    @FXML
    private void handleLogoutButton(){
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();
        try {
            stage = (Stage) logoutButton.getScene().getWindow();
            loader.setLocation(getClass().getResource("./../view/login.fxml"));
            root = (Parent) loader.load();
            LoginController loginController = loader.getController();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes user back to album view
     * */
    @FXML
    private void handleAlbumButton(){
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();

        try {
            stage = (Stage) logoutButton.getScene().getWindow();
            loader.setLocation(getClass().getResource("../view/album.fxml"));
            root = (Parent) loader.load();
            AlbumController albumController = loader.getController();
            albumController.init(u,albumName);
            albumController.start(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Constructs photo's tag list for display
     * */
    private void tagList(){
        Photo p = photoList.get(selectedIndex);
        List<String> tagDisplay = new ArrayList<>();
        HashMap<String, ArrayList<String>> tagsMap = p.getTags();
        for(String key:tagsMap.keySet()){
            tagDisplay.add(key+" : "+ String.join(",",tagsMap.get(key)));
        }
        ObservableList<String> tagDis = FXCollections.observableArrayList(tagDisplay);
        tagList.setItems(tagDis);
    }

    /**
     * Sets date text for display
     * */
    private void settingDateText(){
        dateText.setText("Date: "+photoList.get(selectedIndex).getDate().toString());
    }

    /**
     * Sets caption text for display
     * */
    private void settingCaptionText(){
        captionText.setText("Caption: "+photoList.get(selectedIndex).getCaption());
    }

    /**
     * Displays photo and its information
     * */
    private void display(){
        if(selectedIndex >= 0  && selectedIndex < this.photoList.size()){
            imgView.setImage(getAbsolutePath(photoList.get(selectedIndex).getPath()));
            tagList();
            settingCaptionText();
            settingDateText();
        }
    }
}

