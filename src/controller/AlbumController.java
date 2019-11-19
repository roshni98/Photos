package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import model.Photo;
import model.User;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;

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

    @FXML
    ScrollPane scroller;

    private ObservableList<Photo> obsList;
    private User u; // needed for passing user info
    private Album album;

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

        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.setContent(tilePane);

        // load photos into tilepane
        for(Photo photo : obsList){
            addToTilePane(photo);
        }

    }

    public void start(Stage primaryStage){

    }

    private void addToTilePane(Photo photo){
        VBox v = new VBox();
        ImageView i = new ImageView();
        i.setImage(getPhotoImage(photo));
        i.setFitWidth(40);
        i.setFitHeight(40);
        i.setOnMouseClicked(event ->{
            goToPhotoPage(i, this.album.getPics(), this.album.getPics().indexOf(photo));
        });

        // add imageview
        v.getChildren().add(i);

        // Set caption
        Text caption = new Text(photo.getCaption());
        caption.setStyle("-fx-font-size: 12");
        caption.setOnMouseClicked(mouseEvent -> {
            caption.setText(updateCaption(photo));
        });

        // delete button
        Button deleteButton = new Button();
        deleteButton.setText("delete");
        deleteButton.setOnMouseClicked(mouseEvent -> {
            handleDeletePhotoButton(photo);
        });

        v.getChildren().add(caption);
       // v.getChildren().add(deleteButton);
        v.setAlignment(Pos.BASELINE_CENTER);
        v.setSpacing(10);
        v.setPadding(new Insets(25,30,30,30));
        tilePane.getChildren().add(v);
    }

    //load photo image
    private Image getPhotoImage(Photo photo){
        String photoPath= "/src/model/" + photo.getPath();
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
     * @param photo picture to be deleted
     * */
    @FXML
    public void handleDeletePhotoButton(Photo photo){

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
        try {
            saveObject();
            VBox pane = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void rebuildGrid(){}

}
