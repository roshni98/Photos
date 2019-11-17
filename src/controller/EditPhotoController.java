package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


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
    TextField dateField;

    @FXML
    TextField locationField;

    @FXML
    TextField tagField;

    @FXML
    AnchorPane rootPane;

    @FXML
    public void handlePrevButton(){

    }

    @FXML
    public void handleNextButton(){

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
}

