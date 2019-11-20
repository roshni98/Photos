package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class SearchController {

    @FXML
    Button albumListButton;

    @FXML
    Button createNewAlbumButton;

    @FXML
    Button dateSearchButton;

    @FXML
    Button tagSearch1Button;

    @FXML
    Button tagSearch2Button;

    @FXML
    Button logoutButton;

    @FXML
    TilePane tilePane;

    private User user;
    private ArrayList<Photo> userPhotos;
    private ObservableList<Photo> searchResults;
    private ArrayList<String> tagNames;

    public void start(Stage primaryStage, User user){
        this.user = user;
        this.searchResults = FXCollections.observableArrayList();
        this.userPhotos = new ArrayList<>();
        this.tagNames = new ArrayList<>();
        loadTagsAndPhotos();

        primaryStage.setOnCloseRequest(event -> {
            this.stop();  // Write all changes to data.json
        });
    }

    public void stop(){

    }

    /**
     * Load all user photos into one list and all user tags into another.
     * */
    public void loadTagsAndPhotos(){

        ArrayList<Album> albumList = (ArrayList) this.user.getAlbumList();

        for(Album album : albumList){ // for each album, go through tags
            for(Photo photo : album.getPics()){ // for each photo, go through tags
                if(!this.userPhotos.contains(photo)){
                    this.userPhotos.add(photo);
                }
                for(Map.Entry<String, ArrayList<String>> e : photo.getTags().entrySet()){ // add unique tags to tagNames list
                    String tagName = e.getKey();
                    if(!this.tagNames.contains(tagName)){
                        this.tagNames.add(tagName);
                    }
                }
            }
        }
    }

    @FXML
    public void handleTagSearch1Button(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Dialog");
        dialog.setHeaderText("Tag Search - 1 key/value pair");
        GridPane grid = new GridPane();

        // tag name
        Label tagNameLabel = new Label("Tag Name");
        ComboBox<String> tagNameComboBox = new ComboBox<>();
        tagNameComboBox.setPromptText("Select tag name");
        tagNameComboBox.getItems().addAll(this.tagNames);

        // tag value
        Label tagValLabel = new Label("\nTag Value");
        TextField valueField = new TextField();

        grid.addColumn(1, tagNameLabel, tagNameComboBox, tagValLabel, valueField);
        dialog.getDialogPane().setContent(grid);
        ButtonType searchButton = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(searchButton);

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {
            // search functionality
        }
    }

    @FXML
    public void handleTagSearch2Button(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Dialog");
        dialog.setHeaderText("Tag Search - 2 key/value pairs");
        GridPane grid = new GridPane();

        // tag name 1
        Label tagNameLabel = new Label("First Tag Name");
        ComboBox<String> tagNameComboBox = new ComboBox<>();
        tagNameComboBox.setPromptText("Select tag name");
        tagNameComboBox.getItems().addAll(this.tagNames);

        // tag value 1
        Label tagValLabel = new Label("\nFirst Tag Value\n");
        TextField valueField = new TextField();
        Label newLineLabel = new Label("\n");

        // Radio buttons: AND/OR
        ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("And");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        RadioButton rb2 = new RadioButton("Or");
        rb2.setToggleGroup(group);

        // tag name 2
        Label tagNameLabel2 = new Label("\nSecond Tag Name");
        ComboBox<String> tagNameComboBox2 = new ComboBox<>();
        tagNameComboBox2.setPromptText("Select tag name");
        tagNameComboBox2.getItems().addAll(this.tagNames);

        // tag value 2
        Label tagValLabel2 = new Label("\nSecond Tag Value");
        TextField valueField2 = new TextField();

        grid.addColumn(1, tagNameLabel, tagNameComboBox, tagValLabel, valueField, newLineLabel);
        grid.addColumn(1, rb1, rb2);
        grid.addColumn(1, tagNameLabel2, tagNameComboBox2, tagValLabel2, valueField2);
        dialog.getDialogPane().setContent(grid);
        ButtonType searchButton = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(searchButton);

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) { // search functionality

            // get values
            // key value pair 1
            String tagName1 = tagNameComboBox.getValue();
            String tagValue1 = valueField.getText();

            // and/or
            String conjunction = group.getSelectedToggle().getUserData().toString();

            // key/val pair 2
            String tagName2 = tagNameComboBox2.getValue();
            String tagValue2 = valueField2.getText();
        }
    }

    @FXML
    public void handleDateSearchButton(){

    }

    @FXML
    public void handleCreateNewAlbum(){

    }

    /**
     * Redirects user to login page and saves changes to user accounts
     * */
    @FXML
    public void handleLogoutButton(){
        // current user is no longer admin - redirect to login page

        this.stop(); // write all changes to data.json
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();

        try {
            stage = (Stage) logoutButton.getScene().getWindow();
            loader.setLocation(getClass().getResource("../view/login.fxml"));
            root = (Parent) loader.load();
            LoginController loginController = loader.getController();
            loginController.start(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Redirects user to album list from single album view
     * */
    @FXML
    public void handleAlbumListButton(){
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();
        try {
            saveObject();
            stage = (Stage) albumListButton.getScene().getWindow();
            loader.setLocation(getClass().getResource("./../view/albumList.fxml"));
            root = (Parent) loader.load();
            AlbumListController albumController = loader.getController();
            // passing user object in init
            albumController.init(this.user);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Serializes user object
     * */
    private void saveObject(){
        //updateUserList();
        File file = new File(this.user.getUsername()+".txt");
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            //writing serialized object (create user file if does not exist)
            FileOutputStream fileStream = new FileOutputStream(file); //open file for saving
            ObjectOutputStream out = new ObjectOutputStream(fileStream); //write object into file
            out.writeObject(this.user); // saves user object into file
            out.close();
        }catch (Exception e){
            //e.printStackTrace();
        }
    }

    /**
     * Updates user object with latest album and photo updates
     * */
   /* private void updateUserList(){
        for(int i =0; i< user.getAlbumList().size(); i++){
            if(user.getAlbumList().get(i).getAlbumName().equals(album.getAlbumName())){
                u.getAlbumList().set(i,this.album);
                break;
            }
        }
    }*/
}
