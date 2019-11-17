package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;

import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
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
    private TilePane tilePane;

    @FXML
    ListView<Album> listView;

    @FXML
    Text album_name;


    private int nRows = 200; // number of rows for picture
    private int nCols = 3;  // number of columns


    private ObservableList<Album> obsList;


    public void init(ArrayList<Album> albums) {

        //System.out.println(user.getName());
        //System.out.println("albums size:"+albums.size());

        obsList = FXCollections.observableArrayList(albums);
        listView.setItems(obsList);
        // select the first item if list is not empty
        if (obsList.size() > 0){
            listView.getSelectionModel().select(0);
        }else{
            File directory = new File("./");
            String path = directory.getAbsolutePath()+"/question_mark.jpg";
            File f = new File(path);
            if(f.exists()){
//                album_thumbnail.setImage(new Image("file:"+f.getAbsolutePath()));
            }
        }

        listView
                .getSelectionModel()
                .selectedIndexProperty();
//                .addListener((obs,oldVal,newVal)->
//                        showInfo());

        listView.setCellFactory(param -> new ListCell<Album>(){
            private ImageView imageView;

            public void updateItem(Album a, boolean empty) {
                super.updateItem(a, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Photo tn = a.getThumbnail();
                    File directory = new File("./");
                    String path = tn==null ?directory.getAbsolutePath().substring(0,directory.getAbsolutePath().length()-1)+"/src/model/swift.png": tn.getPath();
                    imageView = new ImageView("file:"+path);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    setText(a.getName());
                    setFont(Font.font(20));
                    setGraphic(imageView);
                }
            }
        });

        // select the first item if list is not empty
        if (obsList.size() > 0){
            listView.getSelectionModel().select(0);
        }else{
//            File directory = new File("./");
//            String path = directory.getAbsolutePath().substring(0,directory.getAbsolutePath().length()-1)+"/src/model/swift.png";
//            File f = new File(path);
//            if(f.exists()){
//                album_thumbnail.setImage(new Image("file:"+f.getAbsolutePath()));
//            }
        }

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

        dialog.setResultConverter(b -> { return (b == buttonAdd) ? new Album(textName.getText()) : null;});
        Optional<Album> result = dialog.showAndWait();

        if(result.isPresent()) {
            Album a = result.get();
            File directory = new File("./");
            Photo p = new Photo(null,null,null);
            p.setPath(directory.getAbsolutePath().substring(0,directory.getAbsolutePath().length()-1)+"/src/model/swift.png");
            p.setlabel(a.getName());
            p.setDate(new Date());
            a.addPic(p);

//            if(a.getName().trim().equals("")){
//               System.out.println("Name cannot be empty");
//            }
//
//            else if(doesAlbumExist(a.getName())){
//                Methods.alert("Album already exists!");
//            }
//            else{
                obsList.add(a);
//                user.addAlbum(result.get());
                try{
                    File file = new File(a.getName());
                    if(file.exists()){
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream fileStream = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fileStream);
//                    out.writeObject(user);
                    out.close();
                } catch (Exception e){
                    //e.printStackTrace();
                }
//            }
        }

//        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(Main.class.getResource("albumList.fxml"));


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
