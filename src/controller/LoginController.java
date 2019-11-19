/**
 * @author Roshni Shah
 * */

package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Album;
import model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class LoginController {

    /**
     * Textfield to enter username
     * */
    @FXML
    TextField usernameField;

    /**
     * Login button
     * */
    @FXML
    Button loginButton;

    /**
     * Main UI panel
     * */
    @FXML
    AnchorPane rootPane;

    /**
     * List of all user objects saved in application
     * */
    private List<User> userObjectList;

    /**
     * Start method to set up login UI and data structures
     * */
    public void start(Stage primaryStage) throws IOException {

        userObjectList = new ArrayList<>();
        /*primaryStage.setOnCloseRequest(event -> {
            this.stop();
        });*/
    }

    /**
     * Handle login when user submits username.
     * Redirects to album list if non-admin user.
     * Redirects to admin panel if admin.
     */
    public void handleLoginButton() throws IOException {
        if(userObjectList == null){
            userObjectList = new ArrayList<>();
        }
        Parent root;
        Stage stage;
        String userInfo = usernameField.getText(); //holds text entered
        JSONParser parser = new JSONParser();
        FXMLLoader loader = new FXMLLoader();

        try {
            stage = (Stage) loginButton.getScene().getWindow();
            if (userInfo.equals("admin")) {
                loader.setLocation(getClass().getResource("../view/admin.fxml"));
                root = (Parent) loader.load();
                AdminController adminController = loader.getController();
                adminController.start(stage);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                return;
            }

            File fil = new File("data.json"); //path of json
            if (fil.length() == 0) return; //file empty, don't load anything
            FileReader fr = new FileReader(fil);
            Object obj = parser.parse(fr);
            JSONArray jsonArray = (JSONArray) obj;
            Iterator<JSONObject> iterator = jsonArray.iterator();

            while (iterator.hasNext()) { // looking through users
                JSONObject currentAccount = iterator.next(); // current user
                String user = (String) currentAccount.get("User");
                createLoadUser(user);

                if (user.equals(userInfo)) {
                    User userObject = null;
                    for(int i =0; i<userObjectList.size(); i++){
                        if(user.equals(userObjectList.get(i).getUsername())){
                            userObject = userObjectList.get(i);
                        }
                    }

                    loader.setLocation(getClass().getResource("./../view/albumList.fxml"));
                    root = (Parent) loader.load();
                    AlbumListController albumListController = loader.getController();

                    // passing user object in init
                    albumListController.init(userObject);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    return;
                }
            }

            Utils.handleDialog(Alert.AlertType.ERROR, "User does not exist! Please try again!", "Error");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates file for user's serialized information (albums and photos)
     * */
    private void createLoadUser(String username) {
        try {
            File file = new File(username+".txt");
            if (!file.exists()) {
                file.createNewFile();
                //writing serialized object (create user file if does not exist)
                FileOutputStream fileStream = new FileOutputStream(file); //open file for saving
                ObjectOutputStream out = new ObjectOutputStream(fileStream); //write object into file
                User u = new User(username);
                out.writeObject(u); // saves user object into file
                out.close();
            }
            //loading serialized object
            FileInputStream fileInputStream = new FileInputStream(file); //open file for reading
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream); //read object from file
            if (fileInputStream.available() > 0) {
                User currentUser = (User) objectInputStream.readObject(); //load object from file to object User
                userObjectList.add(currentUser);
            }
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}




