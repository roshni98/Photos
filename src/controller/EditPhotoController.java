package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Photo;

import java.util.Calendar;


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
    Button saveDate;

    @FXML
    Button saveLocation;

    @FXML
    TextField dateField;

    @FXML
    TextField tagField;

    @FXML
    TableView <String> tagTable;

    @FXML
    TableColumn <String, String>tagNameCol;

    @FXML
    TableColumn <String, String>tagValueCol;

    @FXML
    AnchorPane rootPane;

    @FXML
    public void handlePrevButton(){

    }

    @FXML
    public void handleNextButton(){
//        int i = Photo.getName.getPhotos().indexOf(Photo.currentPhoto);
//        if (i == Photo.getName.getPhotos().size() - 1) {
//            Photo.currentPhoto = Photo.currentAlbum.getPhotos().get(0);
//        } else {
//            Photo.currentPhoto = Photo.currentAlbum.getPhotos().get(i + 1);
//        }

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
    public void handleAlbumListButton(){
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("../view/albumList.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) {
    }
}

