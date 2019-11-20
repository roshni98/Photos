/**
 * @author Amulya Mummaneni
 * */
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @class SearchController allows user to search all their photos using different parameters
 * */
public class SearchController {

    /**
     * Button that takes user back to album list
     * */
    @FXML
    Button albumListButton;

    /**
     * Button that allowed user to create a new album from the most recent search results
     * */
    @FXML
    Button createNewAlbumButton;

    /**
     * Button that allows user to search photos by date range
     * */
    @FXML
    Button dateSearchButton;

    /**
     * Button that allows user to search photos by 1 tag key-value pair
     * */
    @FXML
    Button tagSearch1Button;

    /**
     * Button that allows user to search photos by 2 tag key-value pairs
     * */
    @FXML
    Button tagSearch2Button;

    /**
     * Button that allows user to log out
     * */
    @FXML
    Button logoutButton;

    /**
     * Main UI area for photo display
     * */
    @FXML
    TilePane tilePane;

    /**
     * Allows scrolling through photos in album
     * */
    @FXML
    ScrollPane scroller;

    /**
     * Current user
     * */
    private User user;

    /**
     * Current user's photos
     * */
    private ArrayList<Photo> userPhotos;

    /**
     * List that holds search results for each search
     * */
    private ObservableList<Photo> searchResults;

    /**
     * List of all tagnames user is currently using
     * */
    private ArrayList<String> tagNames;

    /**
     * Start method to set up login UI and data structures
     * @param primaryStage main UI stage
     * @param user current user
     * */
    public void start(Stage primaryStage, User user){
        this.user = user;
        this.searchResults = FXCollections.observableArrayList();
        this.userPhotos = new ArrayList<>();
        this.tagNames = new ArrayList<>();
        loadTagsAndPhotos();

        createNewAlbumButton.setVisible(false);
        tilePane.setPrefColumns(4);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.setContent(tilePane);
        scroller.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);

        primaryStage.setOnCloseRequest(event -> {
            this.stop();  // Write all changes
        });
    }

    /**
     * Method carried out on exit
     * Serializes new information
     * */
    private void stop(){
        saveObject();
    }

    /**
     * Load all user photos into one list and all user tags into another.
     * */
    private void loadTagsAndPhotos(){

        ArrayList<Album> albumList = (ArrayList) this.user.getAlbumList();

        for(Album album : albumList){ // for each album, go through tags
            for(Photo photo : album.getPics()){ // for each photo, go through tags
                if(!isDuplicatePhoto(photo, this.userPhotos)){
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

    /**
     * Allows user to search photos with one tag
     * */
    @FXML
    private void handleTagSearch1Button(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Dialog");
        dialog.setHeaderText("Tag Search - 1 key/value pair");
        GridPane grid = new GridPane();

        // tag name
        Label tagNameLabel = new Label("Tag Name");
        ComboBox<String> tagNameComboBox = new ComboBox<>();
        tagNameComboBox.setPromptText("Select tag name");
        tagNameComboBox.getItems().addAll(this.tagNames);
        tagNameComboBox.setVisibleRowCount(3);

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

            // get values
            String tagName = tagNameComboBox.getValue();
            String tagValue = valueField.getText();

            this.searchResults.clear();

            for(Photo photo : this.userPhotos){
                HashMap<String, ArrayList<String>> tagMap = photo.getTags();
                for(Map.Entry<String, ArrayList<String>> e : tagMap.entrySet()){
                    if(e.getKey().equals(tagName)){ // check tagName
                        for(String tag : e.getValue()){
                            if(tag.equals(tagValue)){ // check tagValue
                                if(!this.searchResults.contains(photo)){
                                    this.searchResults.add(photo);
                                }
                            }
                        }
                    }
                }
            }
            if(this.searchResults.size() > 0){
                createNewAlbumButton.setVisible(true);
                //System.out.println(this.searchResults.get(0).getPath());
                // update tilepane
                tilePane.getChildren().clear();
                for(Photo photo : searchResults){
                    addToTilePane(photo);
                }

            }else{
                createNewAlbumButton.setVisible(false);
            }
        }
    }

    /**
     * Tells whether a photo is in an album
     * @param photo Photo to search for in album
     * @param moveToAlbumPics List of pictures in a given album
     * @return whether photo is in the album
     * */
    private boolean isDuplicatePhoto(Photo photo, List<Photo> moveToAlbumPics){
        for(Photo p : moveToAlbumPics){
            if (photo.getPath().equals(p.getPath())){
                return true;
            }
        }
        return false;
    }

    /**
     * Allows user to search photos with two tag pairs
     * */
    @FXML
    private void handleTagSearch2Button(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Dialog");
        dialog.setHeaderText("Tag Search - 2 key/value pairs");
        GridPane grid = new GridPane();

        // tag name 1
        Label tagNameLabel = new Label("First Tag Name");
        ComboBox<String> tagNameComboBox = new ComboBox<>();
        tagNameComboBox.setPromptText("Select tag name");
        tagNameComboBox.getItems().addAll(this.tagNames);
        tagNameComboBox.setVisibleRowCount(3);

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
        tagNameComboBox2.setVisibleRowCount(3);

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
            RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
            String conjunction = selectedRadioButton.getText();

            // key/val pair 2
            String tagName2 = tagNameComboBox2.getValue();
            String tagValue2 = valueField2.getText();

            int numMatches = 1;
            boolean isAdd = false;
            if(conjunction.equals("And")){
                isAdd = true;
                numMatches = 2;
            }

            this.searchResults.clear();
            for(Photo photo : this.userPhotos){
                HashMap<String, ArrayList<String>> tagMap = photo.getTags();
                for(Map.Entry<String, ArrayList<String>> e : tagMap.entrySet()){
                    if(e.getKey().equals(tagName1)){ // check tagName1
                        for(String tag : e.getValue()){
                            if(tag.equals(tagValue1)){ // check tagValue1
                                numMatches--;
                            }
                        }
                    }
                    if(e.getKey().equals(tagName2)){ // check tagName2
                        for(String tag : e.getValue()){
                            if(tag.equals(tagValue2)){ // check tagValue2
                                numMatches--;
                            }
                        }
                    }
                }
                if(numMatches <= 0){
                    if(!isDuplicatePhoto(photo, this.searchResults)){
                        this.searchResults.add(photo);
                    }
                    if(isAdd){
                        numMatches = 2;
                    }else{
                        numMatches = 1;
                    }

                }
            }

            if(this.searchResults.size() > 0){
                createNewAlbumButton.setVisible(true);
                //System.out.println(this.searchResults.size());
                //System.out.println(this.searchResults.get(0).getPath());
                // add all search results to tilepane
                tilePane.getChildren().clear();
                for(Photo photo : searchResults){
                    addToTilePane(photo);
                }
            }else{
                createNewAlbumButton.setVisible(false);
            }
        }
    }

    /**
     * Allows user to search photos with a date range
     * */
    @FXML
    private void handleDateSearchButton(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Date Search");
        dialog.setHeaderText("Date Search");
        GridPane grid = new GridPane();

        Label label = new Label("Enter desired start and end dates:\n\n");
        Label startLabel = new Label("Start Date:");
        DatePicker startDate = new DatePicker();
        Label endLabel = new Label("End Date:");
        DatePicker endDate = new DatePicker();

        grid.addColumn(1, label, startLabel, startDate, endLabel, endDate);
        dialog.getDialogPane().setContent(grid);

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButton);

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {

            LocalDate beg = startDate.getValue();
            LocalDate end = endDate.getValue();

            Date date1 = Date.from(beg.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date date2 = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());

            this.searchResults.clear();
            for(Photo photo : userPhotos){
                Date currPhotoDate = photo.getDate();
                if(currPhotoDate.compareTo(date1) >= 0 && currPhotoDate.compareTo(date2) <= 0){
                    if(!isDuplicatePhoto(photo, this.searchResults)){
                        this.searchResults.add(photo);
                    }
                }
            }

            if(this.searchResults.size() > 0){
                createNewAlbumButton.setVisible(true);
                // add all search results to tilepane
                tilePane.getChildren().clear();
                for(Photo photo : searchResults){
                    addToTilePane(photo);
                }
            }else{
                createNewAlbumButton.setVisible(false);
            }
        }
    }

    /**
     * Loads image of photo specified
     * @param photo picture to be returned as image
     * @return Image object created from given Photo objet
     * */
    private Image getPhotoImage(Photo photo){
        File f = new File(photo.getPath());
        if(f.exists()) {
            return new Image("file:"+f.getAbsolutePath());
        }
        return null;
    }

    /**
     * Adds thumbnail and caption of given Photo object to tilepane element
     * @param photo current photo
     * */
    private void addToTilePane(Photo photo){
        //currPhoto = photo;
        VBox v = new VBox();
        ImageView i = new ImageView();
        //System.out.println(getPhotoImage(photo));
        i.setImage(getPhotoImage(photo));
        i.setFitWidth(40);
        i.setFitHeight(40);

        // add imageview
        v.getChildren().add(i);

        // Set caption
        Text caption = new Text(photo.getCaption());
        caption.setStyle("-fx-font-size: 12");

        v.getChildren().add(caption);
        v.setAlignment(Pos.BASELINE_CENTER);
        v.setSpacing(10);
        v.setPadding(new Insets(25,30,30,30));
        tilePane.getChildren().add(v);
    }

    /**
     * Allows user to create a new album from the latest search results
     * */
    @FXML
    private void handleCreateNewAlbum(){
        Dialog<Album> dialog = new Dialog<>();
        dialog.setContentText("Add Tag");
        dialog.setHeaderText("Enter new album name:");
        GridPane grid = new GridPane();
        Label labelName = new Label("Name: ");
        TextField textName= new TextField();
        grid.add(labelName, 1, 1);
        grid.add(textName, 2, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);

        dialog.setResultConverter(b -> { return (b == buttonAdd) ? new Album(null, textName.getText()) : null;});
        Optional<Album> result = dialog.showAndWait();

        if(result.isPresent()) {
            for(Album a: user.getAlbumList()){
                if(a.getAlbumName().equals(result.get().getAlbumName())){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Album "+result.get().getAlbumName()+" already exists!");
                    errorAlert.setTitle("Error adding album");
                    errorAlert.showAndWait();
                    return;
                }
            }
            // add photos to album
            Album newAlbum = result.get();
            for(Photo photo : this.searchResults){
                newAlbum.getPics().add(photo);
            }

            // update album's min and max dates
            updateAlbumDates(newAlbum);

            // add album to user's albums
            user.getAlbumList().add(newAlbum);
        }
    }

    /**
     * Updates album date range
     * @param album album to be updated
     * */
    private void updateAlbumDates(Album album){ // update album min and max dates

        if(album.getPics().size() == 0){ // empty album; reset dates
            album.setMin_date(new Date(Long.MIN_VALUE));
            album.setMax_date(new Date(Long.MAX_VALUE));
            return;
        }

        Date minDate = album.getPics().get(0).getDate();
        Date maxDate = album.getPics().get(0).getDate();
        for(Photo photo : album.getPics()){
            if(photo.getDate().compareTo(minDate) < 0){
                minDate = photo.getDate();
            }else if(photo.getDate().compareTo(maxDate) > 0){
                maxDate = photo.getDate();
            }
        }

        album.setMin_date(minDate);
        album.setMax_date(maxDate);
    }

    /**
     * Redirects user to login page and saves changes to user accounts
     * */
    @FXML
    private void handleLogoutButton(){
        // current user is no longer admin - redirect to login page

        this.stop(); // write all changes
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
    private void handleAlbumListButton(){
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
}
