package controller;

import javafx.fxml.FXML;
import model.Photo;

public class EditPhotoController {

    @FXML
    TextField dateField;

    public void start(Stage primaryStage, Photo photo){
        dateField.setText(photo.date());
    }
}
