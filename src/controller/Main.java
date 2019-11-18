package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/login.fxml"));

        VBox root = (VBox) loader.load();

        LoginController controller = loader.getController();

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
