package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

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
    TilePane displayAlbumTile;

    @FXML
    Text album_name;

    @FXML
    ScrollPane scroller;

    private ObservableList<Album> obsList;


    public void init(ArrayList<Album> albums) {
        obsList = FXCollections.observableArrayList(albums);
        displayAlbumTile.setPrefColumns(4);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.setContent(displayAlbumTile);
        updateTilePane();
    }

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

    private void updateTilePane(){
        if(obsList.size() > 0) {
            for (Album a : obsList) {
                VBox v = new VBox();
                ImageView i = new ImageView();
                i.setImage(albumFolderImage());
                i.setFitWidth(40);
                i.setFitHeight(40);
                i.setOnMouseClicked(event ->{
                    goToAlbumPage();
                });
                v.getChildren().add(i);
                v.getChildren().add(new Text(a.getAlbumName()));
                v.setAlignment(Pos.BASELINE_CENTER);
                v.setSpacing(25);
                displayAlbumTile.getChildren().add(v);
            }
        }
    }

    private void addToTilePane(Album a){
        VBox v = new VBox();
        ImageView i = new ImageView();
        i.setImage(albumFolderImage());
        i.setFitWidth(40);
        i.setFitHeight(40);
        i.setOnMouseClicked(event ->{
            goToAlbumPage();
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
        v.getChildren().add(albumNameText);
        v.getChildren().add(albumSizeText);
        v.getChildren().add(albumDateRange);
        v.setAlignment(Pos.BASELINE_CENTER);
        v.setSpacing(10);
        v.setPadding(new Insets(25,30,30,30));
        displayAlbumTile.getChildren().add(v);
    }

    @FXML
    public void handleLogout(){ //directs to login page
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void goToAlbumPage(){
        try {
            VBox pane = FXMLLoader.load(getClass().getResource("../view/album.fxml"));
            rootPane.getChildren().setAll(pane);
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

//    private void openFile(File file) {
//        try {
//            desktop.open(file);
//        } catch (IOException ex) {
//            Logger.getLogger(
//                    FileChooserSample.class.getName()).log(
//                    Level.SEVERE, null, ex
//            );
//        }
//    }


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
        ButtonType buttonAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
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

//    private void showInfo(){
//        //			try{
//        int index = listView.getSelectionModel().getSelectedIndex();
//        if(index == -1 && obsList.size()>0){
//            listView.getSelectionModel().select(0);
//        }else if (obsList.size()>0){
//            Album item = listView.getSelectionModel().getSelectedItem();
//            Photo tn = item.getThumbnail();
//            //System.out.println(tn);
//            File directory = new File("./");
//            String path = tn==null ?directory.getAbsolutePath()+"/question_mark.jpg": tn.getPath();
//
//            if (item != null){
//                album_name.setText(item.getName());
////                album_thumbnail.setImage(new Image("file:"+path));
//                item.resetDates();
////                int size = user.getAlbums().get(user.getAlbums().indexOf(item)).getPhotos().size();
////                album_size.setText(""+size);
////                if(size == 0){
////                    item.resetDates();
////                    //System.out.println("HERE");


    public void start(Stage primaryStage){
        //VBox box = new VBox();

        primaryStage.setTitle("Album List");

//        createAlbumButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//
//            }
//        });

    }


}
