package controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;

import javafx.scene.image.ImageView;

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
//need
//list of photos in album
// photo clicked on by index


    public void init(ArrayList<Photo> pics, int index) {
        this.photoList = pics;
        this.selectedIndex = index;
        if(index >= 0  && index < this.photoList.size()){
            imgView.setImage(new Image(photoList.get(selectedIndex).getPath()));
            tagList();
        }
        obsList = FXCollections.observableArrayList(this.photoList);
    }

    @FXML
    public void handlePrevButton(){
        if(selectedIndex == 0){
            selectedIndex = photoList.size()-1;
        }

        if(selectedIndex > 0  && selectedIndex < this.photoList.size()){
            selectedIndex--;
        }
        imgView.setImage(new Image(photoList.get(selectedIndex).getPath()));
        tagList();
    }

    @FXML
    public void handleNextButton(){

        if(selectedIndex == photoList.size()-1){
            selectedIndex = 0;
        }

        if(selectedIndex > 0  && selectedIndex < this.photoList.size()){
            selectedIndex++;
        }
        imgView.setImage(new Image(photoList.get(selectedIndex).getPath()));
        tagList();
    }

    @FXML
    public void handleLogoutButton(){
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAlbumButton(){
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("../view/album.fxml"));
            rootPane.getChildren().setAll(pane);
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
        dateText.setText(photoList.get(selectedIndex).getDate());
    }

    public void settingCaptionText(){
        captionText.setText(photoList.get(selectedIndex).getCaption());

    }

}

