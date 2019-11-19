package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
     * */
    @FXML
    public static Optional<ButtonType> handleDialog(Alert.AlertType alertType, String contextText, String title) {
        Alert errorAlert = new Alert(alertType);
        errorAlert.setContentText(contextText);
        errorAlert.setTitle(title);
        return errorAlert.showAndWait();
    }

// CAN PROBABLY DELETE

    /**
     * Checks if user is currently in data.json
     * @param user the user you want to check for in data.json
     * @return whether user already has a serializable file
     * */
    public boolean userExists(String user) {
        JSONParser parser = new JSONParser();
        try {
            File fil = new File("data.json"); //path of json
            if (fil.length() == 0) return false; //file empty, don't load anything
            FileReader fr = new FileReader(fil);
            Object obj = parser.parse(fr);
            JSONArray jsonArray = (JSONArray) obj;
            Iterator<JSONObject> iterator = jsonArray.iterator();

            while (iterator.hasNext()) { // looking through users
                JSONObject currentAccount = iterator.next(); // current user
                String currUser = (String) currentAccount.get("User");
                if(user.equals(currUser)){
                    return true;
                }
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
