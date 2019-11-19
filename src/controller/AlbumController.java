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

public class AlbumController {

    @FXML
    AnchorPane rootPane;

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

    //@FXML
   // ScrollPane scroller;

    private ObservableList<Photo> obsList;
    private User u; // needed for passing user info
    private Album album;
    private Photo currPhoto; // currently selected photo

    private ObservableList<String> tags;

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
        tilePane.setPrefColumns(4);
        /*scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.setContent(tilePane);*/

        // load photos into tilepane
        setTilePane();
    }

    public void start(Stage primaryStage){
        copyPhotoButton.setVisible(false);
        movePhotoButton.setVisible(false);
        deletePhotoButton.setVisible(false);
        addTagButton.setVisible(false);
        deleteTagButton.setVisible(false);

        // load all photo's tags
        tags = FXCollections.observableArrayList();

        primaryStage.setOnCloseRequest(event -> {
            this.stop();  // Write all changes to data.json
        });
    }

    // TODO
    private void stop(){
        saveObject();
    }

    private void addToTilePane(Photo photo){
        currPhoto = photo;
        VBox v = new VBox();
        ImageView i = new ImageView();
        System.out.println(getPhotoImage(photo));
        i.setImage(getPhotoImage(photo));
        i.setFitWidth(40);
        i.setFitHeight(40);
        i.setOnMouseClicked(event ->{
            // make appropriate UI buttons visible
            copyPhotoButton.setVisible(true);
            movePhotoButton.setVisible(true);
            deletePhotoButton.setVisible(true);
            addTagButton.setVisible(true);
            deleteTagButton.setVisible(true);

            // set tag list to selected photo's tags
            setTagList(photo); // set tags obslist
            tagList.setItems(tags); // set listview to tags obslist
            tagList.setVisible(true);

            // highlight tile
            v.setBackground(Background.EMPTY);
            String style = "-fx-background-color: rgba(122, 173, 255, 0.5);"; // light blue
            v.setStyle(style);

            // TODO add the following to a new button to redirect to edit photo page
            //goToPhotoPage(i, this.album.getPics(), this.album.getPics().indexOf(photo));
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

    //load photo image
    private Image getPhotoImage(Photo photo){
        String photoPath= photo.getPath();
        File directory = new File("./");
        String path = directory.getAbsolutePath().substring(0,directory.getAbsolutePath().length()-1)+photoPath;
        File f = new File(path);
        if(f.exists()) {
            return new Image("file:"+f.getAbsolutePath());
        }
        return null;
    }

    // if user clicks photo caption in tilepane, they may rename
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

    /**
     * Redirects to single view photo page
     * */
    public void goToPhotoPage(ImageView i, ArrayList<Photo> pics, int index) {
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();

        try {
            stage = (Stage) i.getScene().getWindow();
            loader.setLocation(getClass().getResource("../view/editPhoto.fxml"));
            root = (Parent) loader.load();
            EditPhotoController editPhotoController = loader.getController();
            editPhotoController.init(pics, index);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // event handlers

    /**
     * Delete photo from album
     * */
    @FXML
    public void handleDeletePhotoButton(){
        Optional<ButtonType> result = Utils.handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to DELETE this user?", "Confirmation Dialog");
        if(result.isPresent() && result.get() == ButtonType.OK){
            deletePhoto(currPhoto);
        }
    }

    private void deletePhoto(Photo photo) {
        // remove from observable list
        obsList.remove(photo);

        // reset tilepane?
        setTilePane();

        // remove from album
        this.album.getPics().remove(photo);

    }

    @FXML
    public void handleAddPhotoButton() throws IOException {

        Stage stage = (Stage) addPhotoButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile.getName());
            System.out.println(selectedFile.getAbsolutePath());

            // copy file to model package
            String path = "src/model/"+selectedFile.getName();
            copyFile(selectedFile, new File(path));

            Photo newPhoto = new Photo("", path);

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

            if(result.isPresent()){
                newPhoto.setCaption(textName.getText());
            }

            // add photo to album
            this.album.getPics().add(newPhoto);

            // add to observable list
            obsList.add(newPhoto);

            // add photo to tilepane
            addToTilePane(newPhoto);
        }
    }

    /**
     * Move photo from this album to another album specific by user
     * */
    @FXML
    public void handleMovePhotoButton(){

        // user chooses from dropdown to display other albums (combobox)

        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Move Photo");
        dialog.setHeaderText("Choose destination album: ");
        GridPane grid = new GridPane();
        //Label label = new Label();

        // Combobox of all user's albums
        ComboBox comboBox = new ComboBox();
        for(Album album : u.getAlbumList()){
            if(!album.getAlbumName().equals(this.album.getAlbumName())){
                comboBox.getItems().add(album.getAlbumName());
            }
        }
        grid.add(comboBox, 1, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonAdd = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);

        dialog.setResultConverter(b -> { return (b == buttonAdd) ? comboBox.getValue().toString() : null;});
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) { // result: album name
            // delete photo from old album, observable list

            // add to new album
        }
    }

    // TODO - very similar to move photo, except don't delete from current album
    @FXML
    public void handleCopyPhotoButton(){

    }

    /**
     * Allows user to add tag to current photo.
     * When button is clicked, text field pops up to allow user to type in tag.
     * */
    @FXML
    public void handleAddTagButton(){

    }

    @FXML
    public void handleDeleteTagButton(){

    }

    public void setTagList(Photo photo){
        // load photo tags into observable list
        HashMap<String, ArrayList<String>> photoTags = photo.getTags();
        if(photoTags != null){
            for(Map.Entry<String, ArrayList<String>> tagPair: photoTags.entrySet()){
                for(String tag : tagPair.getValue()){
                    tags.add(tag);
                }
            }
        }
    }

    @FXML
    public void handleEditPhotoButton(){
        this.stop(); // write all changes to data.json
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();

        try {
            stage = (Stage) logoutButton.getScene().getWindow();
            loader.setLocation(getClass().getResource("../view/editPhoto.fxml"));
            root = (Parent) loader.load();
            EditPhotoController editPhotoController = loader.getController();
            editPhotoController.init(this.album.getPics(), this.album.getPics().indexOf(currPhoto));
            //editPhotoController.start(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // serialization

    // updates user object with latest album updates
    // don't change logic, will break saving user
    private void updateUserList(){
        for(int i =0; i< u.getAlbumList().size(); i++){
            if(u.getAlbumList().get(i).getAlbumName().equals(album.getAlbumName())){
                u.getAlbumList().set(i,this.album);
                break;
            }
        }
    }

    //save object to file
    private void saveObject(){
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
    public void handleLogoutButton(){
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

    private void setTilePane() {
        for (Photo photo : obsList) {
            addToTilePane(photo);
        }
    }

    private void copyFile(File src, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);

            // buffer size 1K
            byte[] buf = new byte[1024];

            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
