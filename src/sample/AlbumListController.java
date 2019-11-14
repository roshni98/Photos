package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AlbumListController {
    @FXML
    Button logout;

    @FXML
    Button createAlbumButton;

    @FXML
    Button searchButton;

    @FXML
    Button deleteAlbumButton;

    @FXML
    AnchorPane rootPane;

    @FXML
    public void handleLogout(){ //directs to login page
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("login.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSearchButton(){ //directs to search page
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("search.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage){

    }

}
