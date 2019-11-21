package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Album;

public class Photos extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/login.fxml"));

        VBox root = (VBox) loader.load();

        LoginController controller = loader.getController();

        //Album album = new Album("user", "Album 1");
        controller.start(primaryStage);

        primaryStage.setTitle("Photos");
        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            // Update file
            //controller.stop();
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
