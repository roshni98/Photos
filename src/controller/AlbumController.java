package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AlbumController {

    @FXML
    AnchorPane rootPane;

    @FXML
    Button deletePhotoButton;

    @FXML
    Button movePhotoButton;

    @FXML
    Button addPhotoButton;

    @FXML
    Button copyPhotoButton;

    @FXML
    Button addTagButton;

    @FXML
    Button deleteTagButton;

    @FXML
    Button albumListButton;

    @FXML
    Button logoutButton;

    @FXML
    ListView<String> tagList;

    public void start(Stage primaryStage){

    }

    // event handlers

    @FXML
    public void handleDeletePhotoButton(){

    }

    @FXML
    public void handleMovePhotoButton(){

    }

    @FXML
    public void handleAddPhotoButton(){

    }

    @FXML
    public void handleCopyPhotoButton(){

    }

    /**
     * Allows user to add tag to current album.
     * When button is clicked, text field pops up to allow user to type in tag.
     * */
    @FXML
    public void handleAddTagButton(){

    }

    @FXML
    public void handleDeleteTagButton(){

    }

    /**
     * Redirects user to album list from single album view
     * */
    @FXML
    public void handleAlbumListButton(){
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("../view/albumList.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Logs out user and redirects them to login page
     * */
    @FXML
    public void handleLogoutButton(){
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
