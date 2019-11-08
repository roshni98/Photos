package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminController {

    @FXML
    ListView<String> listView;

    @FXML
    Button createButton;

    @FXML
    Button deleteButton;

    @FXML
    Button logoutButton;

    @FXML
    TextField addUserBox;

    @FXML
    Button addUserButton;

    @FXML
    Button cancelButton;

    @FXML
    Text panelText;

    private ObservableList<String> obsList;

    private List<String> accounts;

    //int songIndex = listView.getSelectionModel().getSelectedIndex();

    public void start(Stage primaryStage){
        addUserButton.setVisible(false);
        addUserBox.setVisible(false);
        cancelButton.setVisible(false);

        accounts = new ArrayList<String>();
        obsList = FXCollections.observableArrayList();
    }

    public void stop(){
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

    @FXML
    public void handleCreateButton(){
        // create new user and write to JSON file
        panelText.setText("Create New User");

        addUserButton.setVisible(true);
        addUserBox.setVisible(true);
        cancelButton.setVisible(true);

        createButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    @FXML
    public void handleDeleteButton(){
        Optional<ButtonType> result = handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to DELETE this user?", "Confirmation Dialog");

        if(result.isPresent() && result.get() == ButtonType.OK){

            int acctIndex = listView.getSelectionModel().getSelectedIndex();
            accounts.remove(acctIndex);
            buildObsList();

            // select different user
            // check next, then prev, else list is empty

            resetUI();

            if(accounts.size() == 0){ // if list is empty
                // disable delete button?
                // TODO make sure its visible when you add user to list
                deleteButton.setVisible(false);
            }

            if(acctIndex >= obsList.size()){
                acctIndex--;
            }

            listView.setItems(obsList);
            listView.getSelectionModel().select(acctIndex);
        }
    }

    @FXML
    public void handleLogoutButton(){ // TODO implement
        // current user is no longer admin
        // redirect to login page
    }

    @FXML
    public void handleAddUserButton(){
        String username = addUserBox.getText();

        // check if it's a duplicate
        if(isDuplicate(username)){ // if so, error popup
            handleDialog(Alert.AlertType.ERROR, "User already exists.", "Duplicate detected");
            return;
        }

        // otherwise, add account
        Optional<ButtonType> result = handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to save?", "Confirm Save");

        if(result.isPresent() && result.get() == ButtonType.OK){
            accounts.add(username);

            buildObsList();

            listView.setItems(obsList);
            listView.getSelectionModel().select(obsList.indexOf(username));

            addUserBox.clear();
            resetUI();
        }
    }

    @FXML
    public void handleCancelButton() {

        Optional<ButtonType> result = handleDialog(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this action?", "Confirmation");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            resetUI();
        }
    }

    @FXML
    public Optional<ButtonType> handleDialog(Alert.AlertType alertType, String contextText, String title){
        Alert errorAlert = new Alert(alertType);
        errorAlert.setContentText(contextText);
        errorAlert.setTitle(title);
        return errorAlert.showAndWait();
    }

    // helpers

    public void buildObsList(){
        obsList.clear();
        for(String user : accounts){
            obsList.add(user.toString());
        }
    }

    public void resetUI(){
        panelText.setText("Manage Users");

        addUserButton.setVisible(false);
        addUserBox.setVisible(false);
        cancelButton.setVisible(false);

        createButton.setVisible(true);
        deleteButton.setVisible(true);
    }

    public boolean isDuplicate(String username){
        for(String user : accounts){
            if(username.equals(user)){
                return true;
            }
        }
        return false;
    }

}
