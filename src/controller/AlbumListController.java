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

public class AlbumListController {
    @FXML
    Button logoutButton;

    @FXML
    Button createAlbumButton;

    @FXML
    Button searchButton;

    @FXML
    AnchorPane rootPane;

    @FXML
    TilePane displayAlbumTile;


    @FXML
    ScrollPane scroller;
    //need observable list for tile pane to update list of albums
    private ObservableList<Album> obsList;
    private User user;
    private List<Album> albums;

    // allow's us to pass albums object from previous page
    public void init(User u) {
        this.user = u;
        this.albums = u.getAlbumList();
        obsList = FXCollections.observableArrayList(u.getAlbumList());
        displayAlbumTile.setPrefColumns(4);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.setContent(displayAlbumTile);
        updateTilePane();
    }

    public void start(Stage primaryStage){
        //VBox box = new VBox();
        // load existing albums
        // initialize list of albums (to maintain in memory)
        primaryStage.setTitle("Album List");
    }

    //load's folder icon
    private Image albumFolderImage(){
        String folderImgFilePath= "/src/model/folder.png";
        File directory = new File("./");
        String path = directory.getAbsolutePath().substring(0,directory.getAbsolutePath().length()-1)+folderImgFilePath;
        File f = new File(path);
        if(f.exists()) {
            return new Image("file:"+f.getAbsolutePath());
        }
        return null;
    }

    //refreshes tile pane
    private void updateTilePane(){
        if(obsList.size() > 0) {
            for (Album a : obsList) {
                addToTilePane(a);
            }
        }
    }

    // delete album
    private void delete(Album a){
        for(int i=0;i<obsList.size();i++){
           if(a.getAlbumName().equals(obsList.get(i).getAlbumName())){
               obsList.remove(i);
               displayAlbumTile.getChildren().remove(i);
            }
        }
    }

    // add's each album to tile pane one by one
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
        Text albumDateRange = new Text(a.getMinDate()+"-"+a.getMaxDate());
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

    // get latest albums and update user object
    private void updateUserList(){
        ArrayList<Album> updatedList = new ArrayList<>();
        for(Album a : obsList){
            updatedList.add(a);
        }
        this.user.setAlbumList(updatedList);
    }

    //save object to file
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


    @FXML
    public void handleLogout() { //directs to login page
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

    @FXML
    public void handleSearchButton(){ //directs to search page
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("../view/search.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // if user clicks filename in tilepane, they may rename
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

    @FXML
    public void createAlbum(){
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
