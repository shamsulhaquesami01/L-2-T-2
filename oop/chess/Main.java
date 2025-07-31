

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    public static Main instance;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        stage = primaryStage;
        stage.setTitle("Network Chess");
        stage.setScene(new SplashScreen().getScene());
        stage.show();
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}