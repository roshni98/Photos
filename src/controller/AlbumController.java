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
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

import java.io.*;
import java.util.*;

/**
 * AlbumController allows user to view one of their albums and make changes to the photos inside
 * */
public class AlbumController {

    /**
     * Main UI panel
     * */
    @FXML
    AnchorPane rootPane;

    /**
     * UI panel for displaying photos
     * */
    @FXML
    TilePane tilePane;

    /**
     * Button to delete photo
     * */
    @FXML
    Button deletePhotoButton;

    /**
     * Button to move photo
     * */
    @FXML
    Button movePhotoButton;

    /**
     * Button to add photo
     * */
    @FXML
    Button addPhotoButton;

    /**
     * Button to copy photo
     * */
    @FXML
    Button copyPhotoButton;

    /**
     * Button to add tag
     * */
    @FXML
    Button addTagButton;

    /**
     * Button to delete tag
     * */
    @FXML
    Button deleteTagButton;

    /**
     * Button to redirect to album list
     * */
    @FXML
    Button albumListButton;

    /**
     * Button to redirect to single photo view
     * */
    @FXML
    Button editPhotoButton;

    /**
     * Logout button
     * */
    @FXML
    Button logoutButton;

    /**
     * Text displaying album name
     * */
    @FXML
    Text albumNameText;

    /**
     * List of photos in album
     * */
    private ObservableList<Photo> obsList;

    /**
     * Current user
     * */
    private User u; // needed for passing user info

    /**
     * Current album being viewed
     * */
    private Album album;

    /**
     * Currently selected photo
     * */
    private Photo currPhoto; // currently selected photo

    /**
     * List to keep track of current photo's tags
     * */
    private ObservableList<String> tags;

    /**
     * Listview for selected photo's tags
     * */
    @FXML
    ListView<String> tagListView;

    /**
     * Allows scrolling through photos in album
     * */
    @FXML
    ScrollPane scroller;

    /**
     * All tag names this user has used on a photo
     * */
    private ArrayList<String> tagNames; // current user's tags

    /**
     * Method to initialize controller items
     * @param u current user
     * @param selectedAlbumName current album
     * */
    public void init(User u, String selectedAlbumName){
        this.u = u;
        for(int i =0; i< u.getAlbumList().size(); i++){
            if(u.getAlbumList().get(i).getAlbumName().equals(selectedAlbumName)){
                this.album = u.getAlbumList().get(i); // set current album
                albumNameText.setText(this.album.getAlbumName()); // set album name on UI
            }
        }

        // populate obsList
        obsList = FXCollections.observableArrayList(this.album.getPics());
        tagNames = new ArrayList<>();
        loadAllTags();

        tilePane.setPrefColumns(4);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.setContent(tilePane);
        scroller.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        // load photos into tilepane
        tilePane.getChildren().clear();
        setTilePane();
    }

    /**
     * Start method to set up login UI and data structures
     * @param primaryStage main UI stage
     * */
    public void start(Stage primaryStage){
        copyPhotoButton.setVisible(false);
        movePhotoButton.setVisible(false);
        deletePhotoButton.setVisible(false);
        addTagButton.setVisible(false);
        deleteTagButton.setVisible(false);
        editPhotoButton.setVisible(false);

        // load all photo's tags
        this.tags = FXCollections.observableArrayList();
        //tagListView.setItems(this.tags);

        primaryStage.setOnCloseRequest(event -> {
            this.stop();  // Write all changes to data.json
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
     * Load all user tags into a list.
     * */
    private void loadAllTags(){

        ArrayList<Album> albumList = (ArrayList) this.u.getAlbumList();

        for(Album album : albumList){ // for each album, go through tags
            for(Photo photo : album.getPics()){ // for each photo, go through tags
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
     * Adds thumbnail and caption of given Photo object to tilepane element
     * @param photo current photo
     * */
    private void addToTilePane(Photo photo){
        currPhoto = photo;
        VBox v = new VBox();
        ImageView i = new ImageView();
        //System.out.println(getPhotoImage(photo) == null);
        i.setImage(getPhotoImage(photo));
        i.setFitWidth(40);
        i.setFitHeight(40);
        i.setOnMouseClicked(event ->{
            currPhoto = photo;
            // make appropriate UI buttons visible
            // make appropriate UI buttons visible
            copyPhotoButton.setVisible(true);
            movePhotoButton.setVisible(true);
            deletePhotoButton.setVisible(true);
            addTagButton.setVisible(true);
            editPhotoButton.setVisible(true);
            deleteTagButton.setVisible(true);
            /*if(this.tags.size() > 0){
                deleteTagButton.setVisible(true);
            }else{
                deleteTagButton.setVisible(false);
            }*/

            // set tag list to selected photo's tags
            setTagListView(photo); // set tags obslist
            tagListView.setItems(this.tags); // set listview to tags obslist
            tagListView.setVisible(true);
            if(this.tags.size() > 0){
                tagListView.getSelectionModel().select(0);
            }
            // highlight tile
            //v.setBackground(Background.EMPTY);
            //String style = "-fx-background-color: rgba(122, 173, 255, 0.5);"; // light blue
           // v.setStyle(style);

        });

        // add imageview
        v.getChildren().add(i);

        // Set caption
        Text caption = new Text(photo.getCaption());
        caption.setStyle("-fx-font-size: 12");
        caption.setOnMouseClicked(mouseEvent -> {
            caption.setText(updateCaption(photo));
        });

        v.getChildren().add(caption);
        v.setAlignment(Pos.BASELINE_CENTER);
        v.setSpacing(10);
        v.setPadding(new Insets(25,30,30,30));
        tilePane.getChildren().add(v);
    }

    /**
     * Loads image of photo specified
     * @param photo picture to be returned as image
     * @return Image object created from given Photo objet
     * */
    private Image getPhotoImage(Photo photo){
        //System.out.println("path: "+photo.getPath());
        File f = new File(photo.getPath());
        if(f.exists()) {
            return new Image("file:"+f.getAbsolutePath());
        }
        return null;
    }

    /**
     * Allows user to update caption if they click it
     * if user clicks photo caption in tilepane, they may rename
     * @param selected currently selected photo
     * @return new caption
     * */
    private String updateCaption(Photo selected){
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Dialog");
        dialog.setHeaderText("Enter new caption: ");
        GridPane grid = new GridPane();
        Label labelName = new Label("Caption: ");
        TextField textName= new TextField();
        grid.add(labelName, 1, 1);
        grid.add(textName, 2, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonAdd = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);

        dialog.setResultConverter(b -> { return (b == buttonAdd) ? textName.getText() : null;});
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {
            selected.setCaption(result.get());
        }
        return selected.getCaption();
    }

    // event handlers

    /**
     * Delete photo from album
     * */
    @FXML
    private void handleDeletePhotoButton(){
        Optional<ButtonType> result = Utils.handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to DELETE this photo?", "Confirmation Dialog");
        if(result.isPresent() && result.get() == ButtonType.OK){

            deletePhoto(currPhoto);
            copyPhotoButton.setVisible(false);
            movePhotoButton.setVisible(false);
            deletePhotoButton.setVisible(false);
            addTagButton.setVisible(false);
            deleteTagButton.setVisible(false);
            editPhotoButton.setVisible(false);

        }
    }

    /**
     * Helper for deleting
     * Removes photo from observable list, resets tilepane, removes photo from album
     * @param photo photo to be deleted
     * */
    private void deletePhoto(Photo photo) {
        // remove from observable list
        obsList.remove(photo);
        album.getPics().remove(photo);
        // reset tilepane
        setTilePane();

        // remove from album
        this.album.getPics().remove(photo);

        // clear tags list and taglistview
        this.tags.clear();
        tagListView.setItems(this.tags);
    }

    /**
     * Adds photo to album
     * */
    @FXML
    private void handleAddPhotoButton() throws IOException {

        Stage stage = (Stage) addPhotoButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();

            // show picture and add caption
            Dialog<String> dialog = new Dialog<>();
            dialog.setContentText("Dialog");
            dialog.setHeaderText("Add caption");

            GridPane grid = new GridPane();
            Label label1 = new Label("Preview");
            Label label2 = new Label("\nCaption: ");
            TextField textName= new TextField();
            ImageView i = new ImageView(new Image(new FileInputStream(selectedFile.getAbsolutePath()), 200, 200, true, true));

            grid.addColumn(1, label1, i, label2, textName);
            dialog.getDialogPane().setContent(grid);

            ButtonType buttonAdd = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonAdd);
            Optional<String> result = dialog.showAndWait();

            Photo newPhoto;
            String caption;

            if(result.isPresent()){
                caption = textName.getText();
                newPhoto = new Photo(caption, path);

                // add photo to album
                this.album.getPics().add(newPhoto);

                // add to observable list
                obsList.add(newPhoto);

                // add photo to tilepane
                addToTilePane(newPhoto);
            }
        }
    }

    /**
     * Copy photo from this album to another album specific by user
     * */
    @FXML
    private void handleCopyPhotoButton(){

        if(u.getAlbumList().size() == 1){
            Utils.handleDialog(Alert.AlertType.ERROR, "No other albums exist to copy photo to.", "No other albums exist");
            return;
        }

        changePhotoLoc(false);
    }

    /**
     * Move photo from this album to another album specific by user
     * */
    @FXML
    private void handleMovePhotoButton(){

        if(u.getAlbumList().size() == 1){
            Utils.handleDialog(Alert.AlertType.ERROR, "No other albums exist to move photo to.", "No other albums exist");
            return;
        }

        changePhotoLoc(true);
    }

    /**
     * Helper with functionality for copying and moving photos
     * @param isMove true if using method to move a photo, false if using to copy
     * */
    private void changePhotoLoc(boolean isMove){

        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Move Photo");
        dialog.setHeaderText("Choose destination album: ");
        GridPane grid = new GridPane();

        // Dropdown of all user's albums
        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText("Select album");
        comboBox.setVisibleRowCount(3);
        for(Album album : u.getAlbumList()){
            if(!album.getAlbumName().equals(this.album.getAlbumName())){
                comboBox.getItems().add(album.getAlbumName());
            }
        }
        grid.add(comboBox, 1, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonAdd = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {

            String moveToAlbum = comboBox.getValue().toString(); // get album name where photo will be copied/moved

            // add to new album
            for(Album album : u.getAlbumList()){
                if(album.getAlbumName().equals(moveToAlbum)){
                    ArrayList<Photo> moveToAlbumPics = album.getPics();
                    if(isDuplicatePhoto(currPhoto, moveToAlbumPics)){ // if photo already in album
                        // error: cannot copy/add add duplicate
                        Utils.handleDialog(Alert.AlertType.ERROR, "Attempted to add a duplicate photo to another folder.", "Duplicate detected");
                        return;
                    }else{
                        if(isMove){ // delete photo from old album, observable list
                            deletePhoto(currPhoto);
                            album.getPics().add(currPhoto);
                        }else{ // copy
                            String caption = currPhoto.getCaption();
                            String path = currPhoto.getPath();
                            Date date = currPhoto.getDate();
                            HashMap<String, ArrayList<String>> tagMapCopy = returnTagMapCopy(currPhoto.getTags());

                            Photo copy = new Photo(caption, path);
                            copy.setDate(date);
                            copy.setTags(tagMapCopy);
                            album.getPics().add(copy);
                        }
                    }
                }
            }

            // confirmation dialog
            if(isMove){ // moved
                Utils.handleDialog(Alert.AlertType.CONFIRMATION, "Photo successfully moved to album \""+moveToAlbum+"\"", "Confirmation Dialog");
            }else{ // copied
                Utils.handleDialog(Alert.AlertType.CONFIRMATION, "Photo successfully copied to album \""+moveToAlbum+"\"", "Confirmation Dialog");
            }

            saveObject();
            //setTilePane();
        }
    }

    /**
     * Tells whether a photo is in an album
     * @param photo Photo to search for in album
     * @param moveToAlbumPics List of pictures in a given album
     * @return whether photo is in the album
     * */
    private boolean isDuplicatePhoto(Photo photo, ArrayList<Photo> moveToAlbumPics){
        for(Photo p : moveToAlbumPics){
            if (photo.getPath().equals(p.getPath())){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a copy of the given hashmap.
     * @param tagMap
     * @return copy of parameter
     * */
    private HashMap<String, ArrayList<String>> returnTagMapCopy(HashMap<String, ArrayList<String>> tagMap){
        HashMap<String, ArrayList<String>> copy = new HashMap<>();
        for(Map.Entry<String, ArrayList<String>> e : tagMap.entrySet()){
            copy.put(e.getKey(), e.getValue());
        }
        return copy;
    }

    /**
     * Allows user to add tag to current photo.
     * When button is clicked, text field pops up to allow user to type in tag.
     * */
    @FXML
    private void handleAddTagButton(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Add Tag");
        dialog.setHeaderText("Enter desired tag: ");
        GridPane grid = new GridPane();

        Label tagType = new Label("Tag Type");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("Choose or add a tag type");
        comboBox.setEditable(true); // user can add their own tag type
        comboBox.getItems().add("Location"); // default tag names
        comboBox.getItems().add("Person");
        for(String tagName : this.tagNames){ // add any other tag names user has
            if(!comboBox.getItems().contains(tagName)){
                comboBox.getItems().add(tagName);
            }
        }
        comboBox.setVisibleRowCount(3);

        Label tagValue = new Label("\nTag Value");
        TextField valueField = new TextField();

        grid.addColumn(1, tagType, comboBox, tagValue, valueField);

        dialog.getDialogPane().setContent(grid);
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButton);

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {

            String key = comboBox.getValue();
            String val = valueField.getText();
            HashMap<String, ArrayList<String>> photoTags = currPhoto.getTags();

            if(key.toLowerCase().equals("location")){
                if(photoTags.containsKey(key)){
                    Utils.handleDialog(Alert.AlertType.ERROR, "Cannot have more than one location.", "Invalid Tag");
                    return;
                }
            }

            // check for duplicate tag
            if(isDuplicateTag(key, val, photoTags)){
                Utils.handleDialog(Alert.AlertType.ERROR, "Attempted to add duplicate tag.", "Duplicate Tag");
                return;
            }

            // add tag to photo hashmap
            ArrayList<String> value = photoTags.get(key);
            if(value == null){
                value = new ArrayList<>();
            }
            value.add(val);
            photoTags.put(key, value);

            // add tag to tagName list
            if(!this.tagNames.contains(key)){
                this.tagNames.add(key);
            }

            // add tag to obs list (tagListView)
            this.tags.add(key+" - "+val);
            tagListView.setItems(this.tags); // set listview to obs list
            tagListView.getSelectionModel().select(this.tags.size() - 1);

            // reset UI
            deleteTagButton.setVisible(true);
        }
    }

    /**
     * Tells whether a tag is a duplicate of the tags on a photo
     * @param key key in tag key-value pair
     * @param value value in tag key-value pair
     * @param tags list of tags on a photo
     * @return whether tag is a duplicate on the photo
     * */
    private boolean isDuplicateTag(String key, String value, HashMap<String, ArrayList<String>> tags){
        for(Map.Entry<String, ArrayList<String>> e : tags.entrySet()){
            for(String tag : e.getValue()){
                if(e.getKey().equals(key) && tag.equals(value)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Allows user to delete tag from current photo.
     * When button is clicked, text field pops up to allow user to type in tag.
     * */
    @FXML
    private void handleDeleteTagButton(){
        int tagIndex = tagListView.getSelectionModel().getSelectedIndex();
        if(tagIndex < 0){ // nothing selected or in list
            Utils.handleDialog(Alert.AlertType.ERROR, "Cannot delete from an empty list.", "Empty tag list");
            return;
        }

        Optional<ButtonType> result = Utils.handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to DELETE this tag?", "Confirmation Dialog");

        if(result.isPresent() && result.get() == ButtonType.OK){

            String[] pair = tags.get(tagIndex).split(" - ");

            // key-value pair to be deleted
            String key = pair[0];
            String value = pair[1];

            boolean removed = false;
            // delete from photo's tag list
            HashMap<String, ArrayList<String>> photoTags = currPhoto.getTags();
            for(Map.Entry<String, ArrayList<String>> e : photoTags.entrySet()){
                for(String tagVal : e.getValue()){
                    if(key.equals(e.getKey()) && value.equals(tagVal)){
                        if(e.getValue().size() > 1){ // if value list is longer than 1
                            e.getValue().remove(tagVal);
                            photoTags.put(key, e.getValue()); // remove tag value from value list and put it back into hashmap
                            removed = true;
                        }else{ // if this is the only value in the value list
                            photoTags.remove(key); //remove entry
                            removed = true;
                        }
                    }
                }
                if(removed){
                    break;
                }
            }

            // delete from observable list
            this.tags.remove(tagIndex);

            // reset listview
            tagListView.setItems(this.tags);

            // select another tag
            if(tags.size() > 0){
                if(tagIndex == 0) tagIndex = 1;
                tagListView.getSelectionModel().select(tagIndex - 1);
            }
        }
    }

    /**
     * Initialize observable list for current photo
     * @param photo current photo
     * */
    private void setTagListView(Photo photo){
        // load photo tags into observable list
        if(this.tags != null){
            this.tags.clear();
            HashMap<String, ArrayList<String>> photoTags = photo.getTags();
            if(photoTags != null){
                for(Map.Entry<String, ArrayList<String>> tagPair: photoTags.entrySet()){
                    for(String tag : tagPair.getValue()){
                        this.tags.add(tagPair.getKey()+" - "+tag);
                    }
                }
            }
        }
    }

    /**
     * Redirects user to single photo view
     * */
    @FXML
    private void handleEditPhotoButton(){
        this.stop(); // write all changes to data.json
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();

        try {
            saveObject();
            stage = (Stage) logoutButton.getScene().getWindow();
            loader.setLocation(getClass().getResource("../view/editPhoto.fxml"));
            root = (Parent) loader.load();
            EditPhotoController editPhotoController = loader.getController();
            editPhotoController.init(this.album.getPics(), this.u, this.album.getAlbumName(), this.album.getPics().indexOf(currPhoto));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // serialization

    /**
     * Updates user object with latest album and photo updates
     * */
    private void updateUserList(){
        for(int i =0; i< u.getAlbumList().size(); i++){
            if(u.getAlbumList().get(i).getAlbumName().equals(album.getAlbumName())){
                u.getAlbumList().set(i,this.album);
                break;
            }
        }
    }

    /**
     * Serializes user object
     * */
    private void saveObject(){
        Utils.updateAlbumDates(this.album);
        updateUserList();
        File file = new File(this.u.getUsername()+".txt");
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            //writing serialized object (create user file if does not exist)
            FileOutputStream fileStream = new FileOutputStream(file); //open file for saving
            ObjectOutputStream out = new ObjectOutputStream(fileStream); //write object into file
            out.writeObject(this.u); // saves user object into file
            out.close();
        }catch (Exception e){
            //e.printStackTrace();
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
            albumController.init(this.u);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Logs out user and redirects them to login page
     * */
    @FXML
    private void handleLogoutButton(){
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();
        try {
            saveObject();
            stage = (Stage) logoutButton.getScene().getWindow();
            loader.setLocation(getClass().getResource("./../view/login.fxml"));
            root = (Parent) loader.load();
            LoginController loginController = loader.getController();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Loads photos from observable list into tile pane element
     * */
    private void setTilePane() {
        tilePane.getChildren().clear();
        for (Photo photo : obsList) {
            addToTilePane(photo);
        }
    }
}
