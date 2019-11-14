package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

public class LoginController {
    private ObservableList<String> objList;
    @FXML
    TextField usernameField;

    @FXML
    Button loginButton;

    public void start(Stage primaryStage){
       // objList = FXCollections.observableArrayList();

    }
    /**
     * Handle login when user submits username
     * */
    public void handleLoginButton(){
        String userInfo = usernameField.getText(); //holds text entered
        JSONParser parser = new JSONParser();

        try{
            File fil = new File("data.json"); //path of json
            if(fil.length() == 0)return; //file empty, don't load anything
            FileReader fr = new FileReader(fil);
            Object obj = parser.parse(fr);
            JSONArray jsonArray = (JSONArray) obj;
            Iterator<JSONObject> iterator = jsonArray.iterator();

            while(iterator.hasNext()){
                JSONObject currentAccount = iterator.next(); // looking through current user
                String user = (String) currentAccount.get("User");

                if(user.equals(userInfo)){
                    //redirect
                    //handleDialog(Alert.AlertType.CONFIRMATION, "User exist!", "Confirmation");

                }
            }
            handleDialog(Alert.AlertType.ERROR, "User does not exist! Please try again!", "Error");


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @FXML
    public Optional<ButtonType> handleDialog(Alert.AlertType alertType, String contextText, String title){
        Alert errorAlert = new Alert(alertType);
        errorAlert.setContentText(contextText);
        errorAlert.setTitle(title);
        return errorAlert.showAndWait();
    }
}




