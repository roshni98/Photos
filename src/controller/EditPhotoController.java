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


public class EditPhotoController {
    @FXML
    Button previousButton;

    @FXML
    Button nextButton;

    @FXML
    Button logoutButton;

    @FXML
    Button albumListButton;

    @FXML
    Text tagText;

    @FXML
    Text dateText;

    @FXML
    Text captionText;

    @FXML
    AnchorPane rootPane;

    @FXML
    private ImageView imgView;

    @FXML
    ListView<String> tagList;

    private List<Photo> photoList;
    private ObservableList<Photo> obsList;
    private int selectedIndex;
    private User u;
    private String albumName;

//need
//list of photos in album
// photo clicked on by index


    public void init(ArrayList<Photo> pics, User u, String albumName, int index) {
        this.photoList = pics;
        this.u = u;
        this.albumName = albumName;
        this.selectedIndex = index;
        obsList = FXCollections.observableArrayList(this.photoList);
        display();
    }

    public Image getAbsolutePath(String urlp){
        File directory = new File(urlp);
        String path = directory.getAbsolutePath();
        File f = new File(path);
        if(f.exists()) {
            return new Image("file:"+f.getAbsolutePath());
        }
        return null;
    }

    @FXML
    public void handlePrevButton(){
        if(selectedIndex == 0){
            selectedIndex = photoList.size()-1;
        }

        if(selectedIndex > 0  && selectedIndex < this.photoList.size()){
            selectedIndex--;
        }
        display();
    }

    @FXML
    public void handleNextButton(){
        if(selectedIndex >= 0  && selectedIndex < this.photoList.size()-1){
            selectedIndex++;
        }
        if(selectedIndex == photoList.size()-1){
            selectedIndex = 0;
        }
        display();
    }

    @FXML
    public void handleLogoutButton(){
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

    @FXML
    public void handleAlbumButton(){
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

    public void start(Stage primaryStage, Photo photo, List<String> pics, int count_place) {
                //List of pics and using count to keep track of which pic on

    }

    public void tagList(){
        Photo p = photoList.get(selectedIndex);
        List<String> tagDisplay = new ArrayList<>();
        HashMap<String, ArrayList<String>> tagsMap = p.getTags();
        for(String key:tagsMap.keySet()){
            tagDisplay.add(key+" : "+ String.join(",",tagsMap.get(key)));
        }
        ObservableList<String> tagDis = FXCollections.observableArrayList(tagDisplay);
        tagList.setItems(tagDis);
    }

    public void settingDateText(){
        dateText.setText("Date: "+photoList.get(selectedIndex).getDate().toString());
    }

    public void settingCaptionText(){
        captionText.setText("Caption: "+photoList.get(selectedIndex).getCaption());

    }

    private void display(){
        if(selectedIndex >= 0  && selectedIndex < this.photoList.size()){
            System.out.println("valid path?: "+photoList.get(selectedIndex).getPath());
            imgView.setImage(getAbsolutePath(photoList.get(selectedIndex).getPath()));
            tagList();
            settingCaptionText();
            settingDateText();
        }
    }

}

