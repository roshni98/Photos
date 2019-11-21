/**
 * @author Amulya Mummaneni
 * */
package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Album;
import model.Photo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

/**
 * Utils class for general methods shared by multiple controllers
 * */
public class Utils {

    /**
     * Generalized method for creating dialog boxes.
     * @param alertType type of alert (e.g. error, warning, etc)
     * @param  contextText Message to be displayed in dialog
     * @param title Title of dialog box
     * @return dialog box
     * */
    @FXML
    public static Optional<ButtonType> handleDialog(Alert.AlertType alertType, String contextText, String title) {
        Alert errorAlert = new Alert(alertType);
        errorAlert.setContentText(contextText);
        errorAlert.setTitle(title);
        return errorAlert.showAndWait();
    }

    /**
     * Updates album's date range
     * @param a album to be updated
     * */
    public static void updateAlbumDates(Album a){ // update album min and max dates

        if(a.getPics().size() == 0){ // empty album; reset dates
            a.setMin_date(new Date(Long.MIN_VALUE));
            a.setMax_date(new Date(Long.MAX_VALUE));
            return;
        }

        Date minDate = a.getPics().get(0).getDate();
        Date maxDate = a.getPics().get(0).getDate();
        for(Photo photo : a.getPics()){
            if(photo.getDate().compareTo(minDate) < 0){
                minDate = photo.getDate();
            }else if(photo.getDate().compareTo(maxDate) > 0){
                maxDate = photo.getDate();
            }
        }

        a.setMin_date(minDate);
        a.setMax_date(maxDate);
    }
}
