package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Album;

import java.util.ArrayList;

public class AlbumController {

    @FXML
    AnchorPane rootPane;

    @FXML
    GridPane gridPane;

    @FXML
    TilePane tilePane;

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
    Text albumNameText;

    @FXML
    ListView<String> tagList;

    private ObservableList<ImageView> obsList;

    public void start(Stage primaryStage, Album album){

       // ArrayList<String> pics = album.getPics(); // photo list

        obsList = FXCollections.observableArrayList();

       /* ScrollPane root = new ScrollPane();
        tilePane.setHgap(10);
*/
        // load all images into obs list
        for(String path : album.getPics()){
            System.out.println("path: "+path);
            Image img = new Image("file:"+path, 100, 100, false, false);

            Group root = new Group(new ImageView(img));
            Scene scene = new Scene(root, 100, 100);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            obsList.add(new ImageView(img));
//            tilePane.getChildren().add(new ImageView(img));
        }

/*
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        root.setFitToWidth(true);
        root.setContent(tilePane);
*/


        // TODO fix this
        // set gridpane to obsList items
        int numCols = gridPane.getColumnCount();
        int row = 0, col = 0;
        for(int i = 0; i < pics.size(); i++, col++){
            if(col == numCols){ // if reached end of one row, reset to next row
                col = 0;
                row++;
            }
            gridPane.add(obsList.get(i), row, col);
            //System.out.println("hello");
        }

        albumNameText.setText(album.getAlbumName());

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

    public void rebuildGrid(){}

}
