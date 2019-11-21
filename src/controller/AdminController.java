/**
 * @author Amulya Mummaneni
 * */

package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdk.jshell.execution.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import controller.Utils;

/**
 * AdminController allows admin access to admin subsystem
 * */
public class AdminController {

    /**
     * Main UI panel
     * */
    @FXML
    AnchorPane rootPane;

    /**
     * List of users
     * */
    @FXML
    ListView<String> listView;

    /**
     * Button to create user
     * */
    @FXML
    Button createButton;

    /**
     * Button to delete user
     * */
    @FXML
    Button deleteButton;

    /**
     * Button to log out admin
     * */
    @FXML
    Button logoutButton;

    /**
     * Textfield to create user
     * */
    @FXML
    TextField addUserBox;

    /**
     * Button to add user
     * */
    @FXML
    Button addUserButton;

    /**
     * Button to cancel adding a user
     * */
    @FXML
    Button cancelButton;

    /**
     * Text displaying current action
     * */
    @FXML
    Text panelText;

    /**
     * List that is updated as changes are made to user list
     * */
    private ObservableList<String> obsList;

    /**
     * In-memory list of users written to memory at end of each session with admin
     * */
    private List<String> accounts;

    /**
     * Start method to set up admin UI and data structures
     * @param primaryStage main UI stage
     * */
    public void start(Stage primaryStage){
        addUserButton.setVisible(false);
        addUserBox.setVisible(false);
        cancelButton.setVisible(false);

        accounts = new ArrayList<String>();
        obsList = FXCollections.observableArrayList();

        loadAccounts();
        listView.setItems(obsList);

        // select the first item
        if(obsList.size() > 0){
            listView.getSelectionModel().select(0);
        }else{
            deleteButton.setVisible(false);
        }

        primaryStage.setOnCloseRequest(event -> {
            this.stop();  // Write all changes to data.json
        });
    }

    /**
     * Stop method to write admin info to memory
     * */
    private void stop(){
        // write all accounts to JSON file
        JSONArray outputList = new JSONArray();

        for (String user : accounts) {
            JSONObject obj = new JSONObject();
            obj.put("User", user);
            outputList.add(obj);
        }

        try (FileWriter file = new FileWriter("data.json")) {
            file.write(outputList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // event handlers

    /**
     * Event handler to create new user
     * */
    @FXML
    private void handleCreateButton(){
        // create new user and write to JSON file
        panelText.setText("Create New User");

        addUserButton.setVisible(true);
        addUserBox.setVisible(true);
        cancelButton.setVisible(true);

        createButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    /**
     * Event handler to delete user
     * */
    @FXML
    private void handleDeleteButton() throws Exception {
        Optional<ButtonType> result = Utils.handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to DELETE this user?", "Confirmation Dialog");

        if(result.isPresent() && result.get() == ButtonType.OK){

            int acctIndex = listView.getSelectionModel().getSelectedIndex();
            String user = accounts.get(acctIndex);
            accounts.remove(acctIndex); // remove user from in-memory list
            buildObsList(); // rebuild obs list
            resetUI();

            // select different user
            // check next, then prev, else list is empty

            if(accounts.size() == 0){ // if list is empty
                deleteButton.setVisible(false); // make delete button invisible
            }

            if(acctIndex >= obsList.size()){
                acctIndex--;
            }

            // update listview
            listView.setItems(obsList);
            listView.getSelectionModel().select(acctIndex);

            // delete user's file from application
            File file = new File(user+".txt"); // find user's serializable file
            if(file.exists()){
                if(!file.delete()){ // delete file
                    throw new Exception("User not deleted"); // throw error if deletion unsuccessful
                }
            }
        }
    }

    /**
     * Redirects user to login page and saves changes to user accounts
     * */
    @FXML
    private void handleLogoutButton(){
        // current user is no longer admin - redirect to login page

        this.stop(); // write all changes to data.json
        Parent root;
        Stage stage;
        FXMLLoader loader = new FXMLLoader();

        try {
            stage = (Stage) logoutButton.getScene().getWindow();
            loader.setLocation(getClass().getResource("../view/login.fxml"));
            root = (Parent) loader.load();
            LoginController loginController = loader.getController();
            loginController.start(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Adds user to application
     * */
    @FXML
    private void handleAddUserButton(){
        String username = addUserBox.getText();

        // check if it's a duplicate
        if(isDuplicate(username)){ // if so, error popup
            Utils.handleDialog(Alert.AlertType.ERROR, "User already exists.", "Duplicate detected");
            return;
        }

        // check if attempting to create admin account
        if(username.toString().equals("admin")){
            Utils.handleDialog(Alert.AlertType.ERROR, "Cannot create admin account.", "Account creation denied");
            return;
        }

        // otherwise, add account
        Optional<ButtonType> result = Utils.handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to save?", "Confirm Save");

        if(result.isPresent() && result.get() == ButtonType.OK){
            accounts.add(username);

            buildObsList();

            listView.setItems(obsList);
            listView.getSelectionModel().select(obsList.indexOf(username));

            addUserBox.clear();
            resetUI();
        }
    }

    /**
     * Handles dialog cancel button
     * */
    @FXML
    private void handleCancelButton() {

        Optional<ButtonType> result = Utils.handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this action?", "Confirmation");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            resetUI();
        }
    }

    // helpers

    /**
     * Helper that rebuilds observable list when changes are made in-memory
     * */
    private void buildObsList(){
        obsList.clear();
        for(String user : accounts){
            obsList.add(user.toString());
        }
    }

    /**
     * Resets UI features to default positions
     * */
    private void resetUI(){
        panelText.setText("Manage Users");

        addUserButton.setVisible(false);
        addUserBox.setVisible(false);
        cancelButton.setVisible(false);

        createButton.setVisible(true);
        deleteButton.setVisible(true);
    }

    /**
     * Checks if user is duplicate
     * @param username username to be checked
     * */
    private boolean isDuplicate(String username){
        for(String user : accounts){
            if(username.equals(user)){
                return true;
            }
        }
        return false;
    }

    /**
     * Loads all user accounts from memory
     * */
    private void loadAccounts() {
        JSONParser parser = new JSONParser();

        try {
            File f = new File("data.json");
            if (f.length() == 0) return; // if file is empty, don't load anything
            FileReader fr = new FileReader(f);
            Object obj = parser.parse(fr);
            JSONArray jsonArray = (JSONArray) obj;
            Iterator<JSONObject> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject currAcct = iterator.next();
                String name = (String) currAcct.get("User");
                accounts.add(name);
            }

            // populate observable list
            buildObsList();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
