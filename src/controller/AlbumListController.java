/**
 * @author Roshni Shah
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
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;

import javafx.scene.image.ImageView;
import model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * AlbumListController Displays list of albums and allows user to edit them
 * */
public class AlbumListController {

    /**
     * Button that allows user to log out
     * */
    @FXML
    Button logoutButton;

    /**
     * Button that allows user to create a new album
     * */
    @FXML
    Button createAlbumButton;

    /**
     * Button that allows user to search their photos
     * */
    @FXML
    Button searchButton;

    /**
     * Main UI panel
     * */
    @FXML
    AnchorPane rootPane;

    /**
     * UI panel where albums are displayed
     * */
    @FXML
    TilePane displayAlbumTile;

    /**
     * Scroller for album display
     * */
    @FXML
    ScrollPane scroller;

    /**
     * Observable list for tile pane to update list of albums
     * */
    private ObservableList<Album> obsList;

    /**
     * Current user
     * */
    private User user;

    /**
     * List of current user's albums
     * */
    private List<Album> albums;

    /**
     * Method to initialize controller items
     * @param u current user
     * */
    public void init(User u) {
        this.user = u;
        this.albums = u.getAlbumList();
        //System.out.println(u.getAlbumList().size());
        if(u.getUsername().equals("stock")){
            for(Album a : this.albums){
                if(a.getAlbumName().equals("stock") && a.getPics().size() < 5){ // if stock photos have not been uploaded
                    a.getPics().add(new Photo("Lady Gaga", System.getProperty("user.dir")+"/ladygaga.jpg"));
                    a.getPics().add(new Photo("One Direction", System.getProperty("user.dir")+"/direction.png"));
                    a.getPics().add(new Photo("Taylor Swift", System.getProperty("user.dir")+"/swift.png"));
                    a.getPics().add(new Photo("Beyonce", System.getProperty("user.dir")+"/beyonce.jpg"));
                    a.getPics().add(new Photo("Jay-Z", System.getProperty("user.dir")+"/jayz.jpg"));
                }
            }
        }
        obsList = FXCollections.observableArrayList(u.getAlbumList());
        displayAlbumTile.setPrefColumns(4);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.setContent(displayAlbumTile);
        scroller.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        updateTilePane();
    }

    /**
     * Start method to set up login UI and data structures
     * @param primaryStage stage for all page elements
     * */
    public void start(Stage primaryStage){
        //VBox box = new VBox();
        // load existing albums
        // initialize list of albums (to maintain in memory)
        primaryStage.setTitle("Album List");
        saveObject();
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
     * loads folder icon for album icon in tilepane
     * @return Image object of folder icon
     * */
    private Image albumFolderImage(){
        String folderImgFilePath= "/folder.png";
        File directory = new File("./");
        String path = directory.getAbsolutePath().substring(0,directory.getAbsolutePath().length()-1)+folderImgFilePath;
        File f = new File(path);
        if(f.exists()) {
            return new Image("file:"+f.getAbsolutePath());
        }
        return null;
    }

    /**
     * Initializes tilepane to display all albums
     * */
    private void updateTilePane(){
        if(obsList.size() > 0) {
            for (Album a : obsList) {
                addToTilePane(a);
            }
        }
    }

    /**
     * Deletes album
     * */
    private void delete(Album a){
        for(int i=0;i<obsList.size();i++){
           if(a.getAlbumName().equals(obsList.get(i).getAlbumName())){
               obsList.remove(i);
               this.user.getAlbumList().remove(a);
               displayAlbumTile.getChildren().remove(i);
            }
        }
    }

    /**
     * Adds one album to tile pane
     * @param a album to be added to display
     * */
    private void addToTilePane(Album a){
        VBox v = new VBox();
        ImageView i = new ImageView();
        i.setImage(albumFolderImage());
        i.setFitWidth(40);
        i.setFitHeight(40);
        i.setOnMouseClicked(event ->{
            goToAlbumPage(i,a.getAlbumName());
        });
        v.getChildren().add(i);
        Text albumNameText = new Text(a.getAlbumName());
        albumNameText.setStyle("-fx-font-size: 12");
        albumNameText.setOnMouseClicked(mouseEvent -> {
                albumNameText.setText(updateAlbum(a));
        });
        Text albumSizeText = new Text("size:"+a.getPics().size());
        albumSizeText.setStyle("-fx-font-size: 10");
        Text albumDateRange = new Text(a.minDateToString()+"-"+a.maxDateToString());
        albumDateRange.setStyle("-fx-font-size: 10");
        Button deleteButton = new Button();
        deleteButton.setText("delete");
        deleteButton.setOnMouseClicked(mouseEvent -> {
            delete(a);
        });
        v.getChildren().add(albumNameText);
        v.getChildren().add(albumSizeText);
        v.getChildren().add(albumDateRange);
        v.getChildren().add(deleteButton);
        v.setAlignment(Pos.BASELINE_CENTER);
        v.setSpacing(10);
        v.setPadding(new Insets(25,30,30,30));
        displayAlbumTile.getChildren().add(v);
    }

    /**
     * Get latest albums and update user object
     * */
    private void updateUserList(){
        ArrayList<Album> updatedList = new ArrayList<>();
        for(Album a : obsList){
            updatedList.add(a);
        }
        this.user.setAlbumList(updatedList);
    }

    /**
     * Save all object to user's file
     * */
    private void saveObject(){
        updateUserList();
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
     * Allows user to log out
     * */
    @FXML
    private void handleLogout() { //directs to login page
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Redirects to single album view
     * @param imageView element in stage to be changed
     * @param albumName album to be viewed in single album view
     * */
    private void goToAlbumPage(ImageView imageView, String albumName){
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();
        try {
            stage = (Stage) imageView.getScene().getWindow();
            loader.setLocation(getClass().getResource("./../view/album.fxml"));
            root = (Parent) loader.load();
            AlbumController albumController = loader.getController();
            updateUserList();
            // passing user object in init
            albumController.init(this.user, albumName);
            albumController.start(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Redirects to search page
     * */
    @FXML
    private void handleSearchButton(){
        try {
            //VBox pane = FXMLLoader.load(getClass().getResource("../view/search.fxml"));
            //rootPane.getChildren().setAll(pane);
            Parent root;
            Stage stage;
            FXMLLoader loader = new FXMLLoader();
            try {
                saveObject();
                stage = (Stage) searchButton.getScene().getWindow();
                loader.setLocation(getClass().getResource("./../view/search.fxml"));
                root = (Parent) loader.load();
                SearchController searchController = loader.getController();
                // passing user object in init
                searchController.start(stage, this.user);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                return;
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Change album name by clicking its name
     * @param selected album to be updated
     * */
    private String updateAlbum(Album selected){
        Dialog <String> dialog = new Dialog<>();
        dialog.setContentText("Dialog");
        dialog.setHeaderText("Enter album name: ");
        GridPane grid = new GridPane();
        Label labelName = new Label("Name: ");
        TextField textName= new TextField();
        grid.add(labelName, 1, 1);
        grid.add(textName, 2, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonAdd = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);

        dialog.setResultConverter(b -> { return (b == buttonAdd) ? textName.getText() : null;});
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {
            for (Album a : obsList) {
                if (a.getAlbumName().equals(result.get())) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Album " + result.get()+ " already exists!");
                    errorAlert.setTitle("Error adding album");
                    errorAlert.showAndWait();
                    return selected.getAlbumName();
                }
            }
            selected.setName(result.get());
        }
        return  selected.getAlbumName();
    }

    /**
     * Allows user to create a new album
     * */
    @FXML
    private void createAlbum(){
        //if the user clicks the create album
        // put folder image down in correct spot and refresh name
        Dialog<Album> dialog = new Dialog<>();
        dialog.setContentText("Dialog");
        dialog.setHeaderText("Enter album name: ");
        GridPane grid = new GridPane();
        Label labelName = new Label("Name: ");
        TextField textName= new TextField();
        grid.add(labelName, 1, 1);
        grid.add(textName, 2, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);


        dialog.setResultConverter(b -> { return (b == buttonAdd) ? new Album(null,textName.getText()) : null;});
        Optional<Album> result = dialog.showAndWait();

        if(result.isPresent()) {
            for(Album a: obsList){
                if(a.getAlbumName().equals(result.get().getAlbumName())){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Album "+result.get().getAlbumName()+" already exists!");
                    errorAlert.setTitle("Error adding album");
                    errorAlert.showAndWait();
                    return;
                }
            }
            obsList.add(result.get());
            addToTilePane(result.get());
        }
    }
}
